package de.htw.lenz.main;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lenz.htw.bogapr.Move;
import lenz.htw.bogapr.net.NetworkClient;

public class Client {
	
	private NetworkClient networkClient;
	private int networkLatencyMillis;
	private int timeLimitSeconds;
	private int playerNumber;
	
	private int[][][] pitch = new int[7][13][3];

	public Client(String clientName) {
		try {
			networkClient = new NetworkClient(null, clientName, ImageIO.read(new File("brain-2-xxl.png")));
			networkLatencyMillis = networkClient.getExpectedNetworkLatencyInMilliseconds();
			timeLimitSeconds = networkClient.getTimeLimitInSeconds();
			playerNumber = networkClient.getMyPlayerNumber();
			
			initializePitch();
			listenForMoves();
	    } catch (IOException e) {
	        throw new RuntimeException("", e);
	    }
	}
	
	
	private void initializePitch() {
		// mark corners
		for (int i = 0; i < 3; i++) {
			this.pitch[0][0][i] = Integer.MIN_VALUE;
			this.pitch[6][0][i] = Integer.MIN_VALUE;
			this.pitch[6][12][i] = Integer.MIN_VALUE;
		}
	}
	private void listenForMoves() {
		while(true) {
			Move receiveMove;
			while ((receiveMove = networkClient.receiveMove()) != null) {
                //Zug in meine Brettrepräsentation einarbeiten
            }
            //berechne tollen Zug
			if(playerNumber == 0) networkClient.sendMove(new Move(0, 1, 3, 2));
		}
	}

}
