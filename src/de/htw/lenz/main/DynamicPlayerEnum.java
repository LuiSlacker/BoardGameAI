package de.htw.lenz.main;

import java.util.Set;
import java.util.TreeSet;

public class DynamicPlayerEnum {

  private Set<Integer> players;
  
  public DynamicPlayerEnum() {
    this.players = new TreeSet<>();
  }

  public Set<Integer> getPlayers() {
    return players;
  }

  public void addPlayer(int player) {
    this.players.add(player);
  }
  
  public void removePlayer(int player) {
    this.players.remove(player);
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
