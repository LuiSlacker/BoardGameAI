package de.htw.lenz.main;

import de.htw.lenz.AI.GameAI;
import de.htw.lenz.AI.MinMaxAI;
import de.htw.lenz.AI.RandomAI;

public class Main3Players {

	public static void main(String[] args) {
		Thread t1 = new Thread(new Task("Luis", new MinMaxAI()));
		t1.start();
		Thread t2 = new Thread(new Task("Mike", new RandomAI()));
		t2.start();
		Thread t3 = new Thread(new Task("Zoey", new RandomAI()));
		t3.start();
	}
}

final class Task implements Runnable {
	
	private String clientName;
	private GameAI gameAI;

	public Task(String clientName, GameAI gameAI) {
		this.clientName = clientName;
		this.gameAI = gameAI;
	}
	
	@Override
	public void run() {
		new Client(this.clientName, null, this.gameAI);
	}
	
}