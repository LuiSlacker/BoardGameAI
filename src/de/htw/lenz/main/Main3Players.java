package de.htw.lenz.main;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import de.htw.lenz.AI.GameAI;
import de.htw.lenz.AI.MinMaxAI;
import de.htw.lenz.AI.RandomAI;

public class Main3Players {
  

	public static void main(String[] args) {
	    Logger logger = Logger.getLogger("MyLog");
	    FileHandler fh;
	    try {
	      // This block configure the logger with handler and formatter
	      fh = new FileHandler("/Users/petulantslacker/Desktop/log.log");
	      logger.addHandler(fh);
	      //System.setProperty("java.util.logging.SimpleFormatter.format",  "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
	      SimpleFormatter formatter = new CustomFormatter();
	      fh.setFormatter(formatter);
	       
	      // the following statement is used to log any messages
	      logger.info("Logger initialized!");
	       
	    } catch (SecurityException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
		Thread t1 = new Thread(new Task("Luis", new MinMaxAI(logger), logger));
		t1.start();
		Thread t2 = new Thread(new Task("Mike", new MinMaxAI(logger), logger));
		t2.start();
		Thread t3 = new Thread(new Task("Zoey", new MinMaxAI(logger), logger));
		t3.start();
	}
}

final class Task implements Runnable {
	
	private String clientName;
	private GameAI gameAI;
	
	private Logger logger;

	public Task(String clientName, GameAI gameAI, Logger logger) {
	    this.logger = logger;
		this.clientName = clientName;
		this.gameAI = gameAI;
	}
	
	@Override
	public void run() {
		new Client(this.clientName, null, this.gameAI, logger);
	}
	
}