package de.htw.lenz.main;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lenz.htw.bogapr.Move;
import lenz.htw.bogapr.net.NetworkClient;

public class Client {
	
	private NetworkClient networkClient;
	private int networkLatencyMillis;
	private int timeLimitMillis;
	private int player;
	private int threadTimeout; 
	private static int ADDITIONAL_SLACK_TIME = 100;
	
	private Pitch pitch;

	public Client(String clientName) {
		try {
		    pitch = new Pitch();
		    
			networkClient = new NetworkClient(null, clientName, ImageIO.read(new File("glasses.png")));
			timeLimitMillis = networkClient.getTimeLimitInSeconds() * 1000;
			networkLatencyMillis = networkClient.getExpectedNetworkLatencyInMilliseconds();
			threadTimeout = timeLimitMillis - (2 * networkLatencyMillis) - ADDITIONAL_SLACK_TIME;
			player = networkClient.getMyPlayerNumber();
			
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
			
			// calculate move
//			Move moveToSend = this.pitch.getRandomMove(player);
//			networkClient.sendMove(moveToSend);
			
			Pitch clonedPitch = null;
			try {
			  clonedPitch = (Pitch)this.pitch.clone();
            } catch (CloneNotSupportedException e1) {
              e1.printStackTrace();
            }
			AICalculation aiCalculation = new AICalculation(clonedPitch, player);
			Thread calculationThread = new Thread(aiCalculation);
			calculationThread.start();
			
			Move moveToSend;
			try {
	          Thread.sleep(threadTimeout);
	          System.out.println("Thread woken up...");
	          moveToSend = aiCalculation.getWisestMove();
	        } catch (InterruptedException e) {
	          moveToSend = this.pitch.getRandomMove(player);
	          System.out.printf("AI-Thread Exception %s", player);
	        } finally {
	          // TODO replace deprecated Thread.stop();
	          calculationThread.stop();
            }
			
			networkClient.sendMove(moveToSend);
			System.out.println(moveToSend);
		}
	}

}

final class AICalculation implements Runnable {
  
  private Pitch pitch;
  private int player;
  private Move currentlyWisestMove;

  public AICalculation(Pitch pitch, int playerNumber) {
      this.pitch = pitch;
      this.player = playerNumber;
  }
  
  @Override
  public void run() {
      this.currentlyWisestMove = this.pitch.getRandomMove(this.player);
//      AI ai = new AI(this.pitch, this.player);
      AI3Players ai = new AI3Players(this.pitch, this.player);
      int depth = 3;
      while(true) {
        this.currentlyWisestMove = ai.getWisestMove(depth);
        System.out.println("Depth:" + depth);
        depth++;
      }
  }
  
  public Move getWisestMove() {
    return this.currentlyWisestMove;
  }
  
}
