package de.htw.lenz.main;

import lenz.htw.bogapr.Move;

public class Node {

  private Move move;
  private double minMaxValue;
  
  public Node(double minMaxValue, Move move) {
    this.move = move;
    this.minMaxValue = minMaxValue;
  }

  public Move getMove() {
    return move;
  }

  public double getMinMaxValue() {
    return minMaxValue;
  }
  
  
}
