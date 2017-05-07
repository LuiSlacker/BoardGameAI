package de.htw.lenz.main;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import lenz.htw.bogapr.Move;
import lenz.htw.bogapr.net.NetworkClient;

public class Client {
	
	private NetworkClient networkClient;
	private int networkLatencyMillis;
	private int timeLimitSeconds;
	private int playerNumber;
	
	private Pitch pitch;
	private AI ai;

	public Client(String clientName) {
		try {
			networkClient = new NetworkClient(null, clientName, ImageIO.read(new File("glasses.png")));
			networkLatencyMillis = networkClient.getExpectedNetworkLatencyInMilliseconds();
			timeLimitSeconds = networkClient.getTimeLimitInSeconds();
			playerNumber = networkClient.getMyPlayerNumber();
			
			pitch = new Pitch();
			ai = new AI(pitch, playerNumber);
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
//			List<Move> possibleMoves = pitch.getPossibleMoves(playerNumber);
//			System.out.println(possibleMoves);
//			Move m = possibleMoves.get((int)(Math.random() * possibleMoves.size()));
//			System.out.println(m);
			
			Move m = ai.getWisestMove();
			System.out.println(m);
			
			networkClient.sendMove(m);
		}
	}

}
