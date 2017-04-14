package de.htw.lenz.main;

public class Main {

	public static void main(String[] args) {
		Thread t1 = new Thread(new Task("Luis"));
		t1.start();
		Thread t2 = new Thread(new Task("Deschanel"));
		t2.start();
		Thread t3 = new Thread(new Task("Relana"));
		t3.start();
	}

}

final class Task implements Runnable {
	
	private String clientName;

	public Task(String clientName) {
		this.clientName = clientName;
	}
	
	@Override
	public void run() {
		Client client = new Client(this.clientName);
	}
	
}