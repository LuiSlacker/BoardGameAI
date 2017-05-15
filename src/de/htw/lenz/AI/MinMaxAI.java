package de.htw.lenz.AI;

import java.util.List;

import de.htw.lenz.main.Pitch;
import lenz.htw.bogapr.Move;

public class MinMaxAI implements GameAI{

  private Pitch pitch;
  private int maximizingPlayer;
  private Move currentlyWisestMove;
  
  private static int INITIAL_DEPTH = 4;

  @Override
  public void start() {
    int depth = INITIAL_DEPTH;
    while(true) {
      getWisestMove(depth);
      System.out.println("Depth:" + depth);
      depth += 3;
    }
  }
  
  public void getWisestMove(int depth){
    double bestValue = miniMax(maximizingPlayer, depth, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
    System.out.println(bestValue);
  }
  
  private double miniMax(int player, int originalDepth, int depth, double alpha, double beta) {
    if (player == maximizingPlayer) {
      return max(player, originalDepth, depth, alpha, beta);
    } else {
      return min(player, originalDepth, depth, alpha, beta);
    }
  }
  
  private double max(int player, int originalDepth, int depth, double alpha, double beta) {
    List<Move> possibleMoves = this.pitch.getPossibleMoves(player);
    if (depth == 0 || possibleMoves.isEmpty()) {
      return this.pitch.assessConfiguration(player);
    }
    double bestValue = alpha;
    for (Move move : possibleMoves) {
      this.pitch.moveChip(move);
      double childValue = miniMax(getNextPlayer(player), originalDepth, depth - 1, bestValue, beta);
      this.pitch.moveChipBack(move);
      if (childValue > bestValue) {
        bestValue = childValue;
        if (bestValue >= beta) break;
        if (depth == originalDepth) this.currentlyWisestMove = move;
      }
    }
    return bestValue;
  }
  
  private double min(int player, int originalDepth, int depth, double alpha, double beta) {
    List<Move> possibleMoves = this.pitch.getPossibleMoves(player);
    if (depth == 0 || possibleMoves.isEmpty()) {
      return this.pitch.assessConfiguration(player);
    }
    double bestValue = beta;
    for (Move move : possibleMoves) {
      this.pitch.moveChip(move);
      double childValue = miniMax(getNextPlayer(player), originalDepth, depth - 1, alpha, bestValue);
      this.pitch.moveChipBack(move);
      if (childValue < bestValue) {
        bestValue = childValue;
        if (bestValue <= alpha) break;
      }
    }
    return bestValue;
  }
  
  private int getNextPlayer(int player) {
    return (player + 1) % 3;
  }
  
  @Override
  public Move getMove() {
    return this.currentlyWisestMove;
  }

  @Override
  public void setPitch(Pitch pitch) {
    this.pitch = pitch;
  }

  @Override
  public void setPlayer(int player) {
    this.maximizingPlayer = player;
  }

}
