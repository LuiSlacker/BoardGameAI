package de.htw.lenz.main;

public enum Player {
  PLAYER1, PLAYER2, PLAYER3;
  
  private static Player[] vals = values();

  public Player next() {
    return vals[(this.ordinal()+1) % vals.length];
  }


}
