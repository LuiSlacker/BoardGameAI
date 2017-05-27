package de.htw.lenz.AI;

import de.htw.lenz.main.DynamicPlayerEnum;
import de.htw.lenz.main.Pitch;
import lenz.htw.bogapr.Move;

/**
 * This class only exists to mock a player being kicked out of the game to observe the impact on the MinMax-algorithm
 * 
 */
public class ErrorAI implements GameAI{

  @Override
  public void setPitch(Pitch pitch) {}

  @Override
  public void setPlayer(int player) {}

  @Override
  public void start() {}

  @Override
  public Move getMove() {
    return null;
  }

  @Override
  public void setPlayers(DynamicPlayerEnum players) {}

}
