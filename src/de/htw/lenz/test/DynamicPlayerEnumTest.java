package de.htw.lenz.test;

import org.junit.Test;

import de.htw.lenz.main.DynamicPlayerEnum;
import static org.junit.Assert.assertEquals;

public class DynamicPlayerEnumTest {

  @Test
  public void getNextPlayerTest() {
    DynamicPlayerEnum players = new DynamicPlayerEnum();
    assertEquals(1, players.getNextPlayer(0));
    assertEquals(2, players.getNextPlayer(1));
    assertEquals(0, players.getNextPlayer(2));
  }
  
  @Test
  public void getNextPlayerForTwoPlayersTest() {
    DynamicPlayerEnum players = new DynamicPlayerEnum();
    players.removePlayer(0);
    assertEquals(2, players.getNextPlayer(1));
    assertEquals(1, players.getNextPlayer(2));
    
    players = new DynamicPlayerEnum();
    players.removePlayer(1);
    assertEquals(2, players.getNextPlayer(0));
    assertEquals(0, players.getNextPlayer(2));
    
    players = new DynamicPlayerEnum();
    players.removePlayer(2);
    assertEquals(1, players.getNextPlayer(0));
    assertEquals(0, players.getNextPlayer(1));
  }
  
  @Test
  public void getNextPlayerForOnePlayerTest() {
    DynamicPlayerEnum players = new DynamicPlayerEnum();
    players.removePlayer(1);
    players.removePlayer(0);
    assertEquals(2, players.getNextPlayer(2));
    
    players = new DynamicPlayerEnum();
    players.removePlayer(2);
    players.removePlayer(1);
    assertEquals(0, players.getNextPlayer(0));
    
    players = new DynamicPlayerEnum();
    players.removePlayer(2);
    players.removePlayer(0);
    assertEquals(1, players.getNextPlayer(1));
  }

}
