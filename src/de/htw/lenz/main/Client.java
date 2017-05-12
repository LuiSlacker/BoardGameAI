package de.htw.lenz.main;

import java.io.File;
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
	private String clientName;
	private int threadTimeout; 
	private GameAI gameAI;
	private static int ADDITIONAL_SLACK_TIME = 100;
	
	private Pitch pitch;

	public Client(String clientName, GameAI gameAI) {
		try {
		    this.gameAI = gameAI;
		    this.clientName = clientName;
		    pitch = new Pitch();
		    
			networkClient = new NetworkClient(null, clientName, ImageIO.read(new File("glasses.png")));
			timeLimitMillis = networkClient.getTimeLimitInSeconds() * 1000;
			networkLatencyMillis = networkClient.getExpectedNetworkLatencyInMilliseconds();
			threadTimeout = timeLimitMillis - (2 * networkLatencyMillis) - ADDITIONAL_SLACK_TIME;
			player = networkClient.getMyPlayerNumber();
			gameAI.setPlayer(player);
			
			listenForMoves();
	    } catch (IOException e) {
	        throw new RuntimeException("", e);
	    }
	}
	
	private void listenForMoves() {
		while(true) {
			Move receiveMove;
			while ((receiveMove = networkClient.receiveMove()) != null) {
			  pitch.moveChip(receiveMove);
            }
			
			gameAI.setPitch(deepClonePitch());
			AIRunnable aiRunnable = new AIRunnable(gameAI);
			Thread calculationThread = new Thread(aiRunnable);
			calculationThread.start();
			
			Move moveToSend;
			try {
	          Thread.sleep(threadTimeout);
	          moveToSend = aiRunnable.getMove();
	        } catch (InterruptedException e) {
	          moveToSend = this.pitch.getRandomMove(player);
	          System.out.printf("AI-Thread Exception %s", player);
	        } finally {
	          // TODO replace deprecated Thread.stop();
	          calculationThread.stop();
            }
			
			System.out.printf("Sending move %s for %s", moveToSend, clientName);
			System.out.println("----");
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
