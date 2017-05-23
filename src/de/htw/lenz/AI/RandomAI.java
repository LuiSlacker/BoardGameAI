package de.htw.lenz.AI;

import de.htw.lenz.main.Pitch;
import lenz.htw.bogapr.Move;

public class RandomAI implements GameAI{
  
  private Pitch pitch;
  private int player;
  private Move move;
  
  @Override
  public void start() {
    this.move = this.pitch.getRandomMove(this.player);
  }
  
  @Override
  public Move getMove() {
    return this.move;
  }

  @Override
  public void setPitch(Pitch pitch) {
    this.pitch = pitch;
  }

  @Override
  public void setPlayer(int player) {
    this.player = player;
  }

  @Override
  public void observePlayers(int player) {}

}
  
