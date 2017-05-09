package de.htw.lenz.main;

import java.util.List;

import lenz.htw.bogapr.Move;

public class AI3Players {

  private Pitch pitch;
  private int maximizingPlayer;

  public AI3Players(Pitch pitch, int player) {
    this.pitch = pitch;
    this.maximizingPlayer = player;
  }
  
  public Move getWisestMove(int depth){
    Move m = miniMax(maximizingPlayer, depth, depth).getMove();
    return m;
    
  }
  
  private Node miniMax(int player, int originalDepth, int depth) {
    List<Move> possibleMoves = this.pitch.getPossibleMoves(player);
    if (depth == 0 || possibleMoves.isEmpty()) {
      return new Node(this.pitch.assessConfiguration(player), null);
    } else if (player == maximizingPlayer) {
      return max(player, depth, originalDepth, possibleMoves);
    } else {
      return min(player, depth, originalDepth, possibleMoves);
    }
  }
  
  private Node max(int player, int depth, int originalDepth, List<Move> possibleMoves){
    double bestValue = Integer.MIN_VALUE;
    Move bestMove = null;
    for (Move move : possibleMoves) {
      this.pitch.moveChip(move);
      double childValue = miniMax(getNextPlayer(player), originalDepth, depth - 1).getMinMaxValue();
      bestValue = Math.max(bestValue, childValue);
      if (bestValue == childValue && depth == originalDepth) bestMove = move;
      this.pitch.moveChipBack(move);
    }
    return new Node(bestValue, bestMove);
  }
  
  private Node min(int player, int depth, int originalDepth, List<Move> possibleMoves){
    double bestValue = Integer.MAX_VALUE;
    Move bestMove = null;
    for (Move move : possibleMoves) {
      this.pitch.moveChip(move);
      double childValue = miniMax(getNextPlayer(player), originalDepth, depth - 1).getMinMaxValue();
      bestValue = Math.min(bestValue, childValue);
      //if (bestValue == childValue && depth == originalDepth) bestMove = move;
      this.pitch.moveChipBack(move);
    }
    return new Node(bestValue, bestMove);
  }
  
  private int getNextPlayer(int player) {
    return (player + 1) % 3;
  }
  
}
