package de.htw.lenz.main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import lenz.htw.bogapr.Move;
import lenz.htw.bogapr.net.NetworkClient;

public class Client {
	
	private NetworkClient networkClient;
	private int networkLatencyMillis;
	private int timeLimitSeconds;
	private int playerNumber;
	
	private Pitch pitch;

	public Client(String clientName) {
		try {
			networkClient = new NetworkClient(null, clientName, ImageIO.read(new File("glasses.png")));
			networkLatencyMillis = networkClient.getExpectedNetworkLatencyInMilliseconds();
			timeLimitSeconds = networkClient.getTimeLimitInSeconds();
			playerNumber = networkClient.getMyPlayerNumber();
			
			pitch = new Pitch();
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
			if(playerNumber == 0) networkClient.sendMove(new Move(0, 1, 3, 2));
			if(playerNumber == 1) networkClient.sendMove(new Move(0, 5, 1, 4));
			if(playerNumber == 2) networkClient.sendMove(new Move(10, 5, 7, 5));
		}
	}

}
