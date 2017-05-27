package de.htw.lenz.AI;

import de.htw.lenz.gameUtils.DynamicPlayerEnum;
import de.htw.lenz.gameUtils.Pitch;
import lenz.htw.bogapr.Move;

public interface GameAI {

  public void setPitch(Pitch pitch);
  public void setPlayer(int player);
  public void start();
  public Move getMove();
  public void setPlayers(DynamicPlayerEnum players);
}
