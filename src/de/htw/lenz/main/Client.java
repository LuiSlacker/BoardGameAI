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
	
	private int[][] pitch = new int[47][3];
	
	private static final int INVALID = Integer.MIN_VALUE; 
	private static final int PLAYER1 = 1; 
	private static final int PLAYER2 = 2; 
	private static final int PLAYER3 = 3; 

	public Client(String clientName) {
		try {
			networkClient = new NetworkClient(null, clientName, ImageIO.read(new File("brain-2-xxl.png")));
			networkLatencyMillis = networkClient.getExpectedNetworkLatencyInMilliseconds();
			timeLimitSeconds = networkClient.getTimeLimitInSeconds();
			playerNumber = networkClient.getMyPlayerNumber();
			
			initializePitch();
			printPitch();
			listenForMoves();
	    } catch (IOException e) {
	        throw new RuntimeException("", e);
	    }
	}
	
	private void initializePitch() {
		
		// invalidate top lefthand corner
		this.pitch[35][0] = INVALID;
		this.pitch[35][1] = INVALID;
		this.pitch[35][2] = INVALID;
		
		// set initial player chips
		intializeSingleFieldWithPlayer(mapCoordinatesToIndex(0,1), PLAYER1);
		intializeSingleFieldWithPlayer(mapCoordinatesToIndex(1,1), PLAYER1);
		intializeSingleFieldWithPlayer(mapCoordinatesToIndex(2,1), PLAYER1);
		
		intializeSingleFieldWithPlayer(mapCoordinatesToIndex(1,6), PLAYER2);
		intializeSingleFieldWithPlayer(mapCoordinatesToIndex(2,6), PLAYER2);
		intializeSingleFieldWithPlayer(mapCoordinatesToIndex(0,5), PLAYER2);
		
		intializeSingleFieldWithPlayer(mapCoordinatesToIndex(10,6), PLAYER3);
		intializeSingleFieldWithPlayer(mapCoordinatesToIndex(11,6), PLAYER3);
		intializeSingleFieldWithPlayer(mapCoordinatesToIndex(10,5), PLAYER3);
	}
	
	private void intializeSingleFieldWithPlayer(int index, int player){
		for (int i = 0; i < 3; i++) {
			this.pitch[index][i] = player;
		}
	}
	
	private static int mapCoordinatesToIndex(int x, int y) {
		return x + rowOffset(y);
	}
	
	private static int rowOffset(int rowNumber) {
		rowNumber--;
		return (rowNumber + 2) * rowNumber;
	}
	
	private void printPitch() {
		System.out.println(Arrays.deepToString(this.pitch));
	}
	
	/**
	 * handles one move of a chip
	 * @param move the move to be taken care of
	 */
	private void moveChip(Move move) {
		int fromIndex  = mapCoordinatesToIndex(move.fromX, move.fromY);
		int toIndex  = mapCoordinatesToIndex(move.toX, move.toY);
		int player = takeChip(fromIndex);
		setChip(toIndex, player);
	}
	
	/**
	 * removes the chip which is on top of the "stack" of a field and returns the corresponding player
	 * NOTE: No validation of move necessary since server only sends valid moves
	 * 
	 * @param index index of the field (internal array representation) to take the chip from
	 * @return the player of the top most chip of the "stack" of that field
	 */
	private int takeChip(int index) {
		int player = INVALID;
		for (int i = 2; i >= 0; i--) {
			if (this.pitch[index][i] != 0) {
				player = this.pitch[index][i];
				this.pitch[index][i] = 0;
				break;
			};
		}
		return player;
	}
	
	/**
	 * sets a chip of player on top of the "stack" of a field
	 *  
	 * @param index index of the field (internal array representation) to set the chip
	 * @param player player to set the chip for
	 */
	private void setChip(int index, int player) {
		for (int i = 0; i <= 2; i++) {
			if (this.pitch[index][i] == 0) {
				this.pitch[index][i] = player;
				break;
			}
		}
	}
	
	private void listenForMoves() {
		while(true) {
			Move receiveMove;
			while ((receiveMove = networkClient.receiveMove()) != null) {
                moveChip(receiveMove);
                printPitch();
            }
            //berechne tollen Zug
			if(playerNumber == 0) networkClient.sendMove(new Move(0, 1, 3, 2));
		}
	}

}
