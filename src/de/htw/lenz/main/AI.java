package de.htw.lenz.main;

import java.util.List;

import lenz.htw.bogapr.Move;

public class AI {
  
  private Pitch pitch;
  private int player;
  private static final int DEPTH = 5;

  public AI(Pitch pitch, int player) {
    this.pitch = pitch;
    this.player = player +1 ;
  }
  
  public Move getWisestMove(){
    return negamax(this.pitch, DEPTH, DEPTH).getMove();
    
  }
  
  private Node negamax(Pitch pitch , int originalDepth, int depth) {
    List<Move> possibleMoves = pitch.getPossibleMoves(this.player);
    if (depth == 0 || possibleMoves.isEmpty()) {
      return new Node(this.pitch.assessConfiguration(this.player), null);
    } else {
      double bestValue = Integer.MIN_VALUE;
      Move bestMove = null;
      
      for (Move move : possibleMoves) {
        this.pitch.moveChip(move);
        double childValue = -negamax(pitch, originalDepth, depth -1).getMinMaxValue();
        bestValue = Math.max(bestValue, childValue);
        if (bestValue == childValue && depth == originalDepth) bestMove = move;
        this.pitch.moveChipBack(move);
      }
      return new Node(bestValue, bestMove);
    }
  }
  
  
}
