package de.htw.lenz.AI;

import de.htw.lenz.main.Pitch;
import lenz.htw.bogapr.Move;

public interface GameAI {

  public void setPitch(Pitch pitch);
  public void setPlayer(int player);
  public void start();
  public Move getMove();
}
