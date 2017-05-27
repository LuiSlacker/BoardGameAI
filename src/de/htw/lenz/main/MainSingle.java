package de.htw.lenz.main;

import de.htw.lenz.AI.MinMaxAI;
import de.htw.lenz.gameUtils.Client;

/**
 * Single Player MinMaxAI to be packaged into  jar
 *
 */
public class MainSingle {

  public static void main(String[] args) {
    if (args.length < 1) throw new Error("no host provided");
    new Client("Luis", args[0], new MinMaxAI());
  }

}
