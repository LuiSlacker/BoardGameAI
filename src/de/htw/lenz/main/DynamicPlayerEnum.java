package de.htw.lenz.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DynamicPlayerEnum {

  private List<Integer> players;
  private int previousPlayer = 2;
  
  public DynamicPlayerEnum() {
    this.players = new ArrayList<>(Arrays.asList(0, 1, 2));
  }

  public List<Integer> getPlayers() {
    return players;
  }

  public void removePlayer(int player) {
    this.players.remove(player);
  }
  
  public void observePlayers(int player) {
    System.out.println("player: " + player);
    System.out.println("prevplayer: " + this.previousPlayer);
    System.out.println("size: " + this.players.size());
    if (this.players.size() == 3) {
      if (player == getSecondNextPlayer(this.previousPlayer)) {
        System.out.println("removing next player");
        this.players.remove(getNextPlayerIndex(this.previousPlayer));
      } else if (player == this.previousPlayer ) {
        System.out.println("removing both");
        int nextPlayer1 = getNextPlayerIndex(this.previousPlayer);
        System.out.println(nextPlayer1);
        this.players.remove(nextPlayer1);
        int nextplayer2 = getNextPlayerIndex(this.previousPlayer);
        System.out.println(nextplayer2);
        this.players.remove(nextplayer2);
      }
    } else if(this.players.size() == 2 && player == this.previousPlayer) {
      int nextPLayer = getNextPlayerIndex(this.previousPlayer);
      System.out.println(nextPLayer);
      this.players.remove(nextPLayer);
    }
    this.previousPlayer = player;
  }
  
  public int getNextPlayer(int player) {
    int nextPlayersIndex = (this.players.indexOf(player) + 1) % this.players.size();
    return this.players.get(nextPlayersIndex);
  }
  
  public int getNextPlayerIndex(int player) {
    return (this.players.indexOf(player) + 1) % this.players.size();
  }
  
  public int getSecondNextPlayer(int player) {
    int secondNextPlayersIndex = (this.players.indexOf(player) + 2) % this.players.size();
    return this.players.get(secondNextPlayersIndex);
  }
  
  public int getLength() {
    return this.players.size();
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (int player : players) {
      sb.append(player);
      sb.append(",");
    }
    sb.append(")");
    return sb.toString();
  }
  
}
