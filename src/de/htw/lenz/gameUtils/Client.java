package de.htw.lenz.gameUtils;

import java.io.IOException;

import javax.imageio.ImageIO;

import de.htw.lenz.AI.GameAI;
import lenz.htw.bogapr.Move;
import lenz.htw.bogapr.net.NetworkClient;

public class Client {
	
	private NetworkClient networkClient;
	private int networkLatencyMillis;
	private int timeLimitMillis;
	private int player;
	private int threadTimeout; 
	private GameAI gameAI;
	private static int ADDITIONAL_SLACK_TIME = 400;
	private Pitch pitch;
	private DynamicPlayerEnum players;

	public Client(String clientName, String host, GameAI gameAI) {
		try {
		    this.gameAI = gameAI;
		    this.players = new DynamicPlayerEnum();
		    this.pitch = new Pitch(players);
		    
			networkClient = new NetworkClient(host, clientName, ImageIO.read(getClass().getResourceAsStream("glasses.png")));
			timeLimitMillis = networkClient.getTimeLimitInSeconds() * 1000;
			networkLatencyMillis = networkClient.getExpectedNetworkLatencyInMilliseconds();
			threadTimeout = timeLimitMillis - (2 * networkLatencyMillis) - ADDITIONAL_SLACK_TIME;
			player = networkClient.getMyPlayerNumber();
			gameAI.setPlayer(player);
			gameAI.setPlayers(players);
			
			listenForMoves();
	    } catch (IOException e) {
	        throw new RuntimeException("", e);
	    }
	}
	
	private void listenForMoves() {
		while(true) {
			Move receiveMove;
			while ((receiveMove = networkClient.receiveMove()) != null) {
			  int playerFromMove = pitch.moveChip(receiveMove);
			  this.players.observePlayers(playerFromMove);
            }
			
			/** Calculate best Move **/
			gameAI.setPitch(deepClonePitch());
			AIRunnable aiRunnable = new AIRunnable(gameAI);
			Thread calculationThread = new Thread(aiRunnable);
			calculationThread.start();
			
			// Initialize moveToSend with a random Move
			Move moveToSend = this.pitch.getRandomMove(player);
			try {
	          Thread.sleep(threadTimeout);
	          // once the time for calculation is reached, retrieve the currently best Move from AI
	          moveToSend = aiRunnable.getMove();
	        } catch (InterruptedException e) {
	          moveToSend = this.pitch.getRandomMove(player);
	          System.out.printf("AI-Thread Exception %s", player);
	        } finally {
	          // Stops the running Thread
	          calculationThread.stop();
            }
			
			networkClient.sendMove(moveToSend);
		}
	}
	
	private Pitch deepClonePitch() {
	  Pitch clonedPitch = null;
      try {
        clonedPitch = this.pitch.clone();
      } catch (CloneNotSupportedException e1) {
        e1.printStackTrace();
      }
      return clonedPitch;
	}

}

/**
 * Runnable for the AI-Calculation Thread
 *
 */
final class AIRunnable implements Runnable {
  
  private GameAI gameAI;

  public AIRunnable(GameAI gameAI) {
      this.gameAI = gameAI;
  }
  
  @Override
  public void run() {
    gameAI.start();
  }
  
  public Move getMove() {
    return this.gameAI.getMove();
  }
  
}
