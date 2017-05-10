package de.htw.lenz.AI;

import java.util.List;

import de.htw.lenz.main.Node;
import de.htw.lenz.main.Pitch;
import lenz.htw.bogapr.Move;

public class MinMaxAI implements GameAI{

  private Pitch pitch;
  private int maximizingPlayer;
  private Move currentlyWisestMove;
  
  private static int INITIAL_DEPTH = 3;

  @Override
  public void start() {
    int depth = INITIAL_DEPTH;
    while(true) {
      currentlyWisestMove = getWisestMove(depth);
      System.out.println("Depth:" + depth);
      depth++;
    }
  }
  
  @Override
  public Move getMove() {
    return this.currentlyWisestMove;
  }
  
  public Move getWisestMove(int depth){
    return miniMax(maximizingPlayer, depth, depth).getMove();
    
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
      this.pitch.moveChipBack(move);
    }
    return new Node(bestValue, bestMove);
  }
  
  private int getNextPlayer(int player) {
    return (player + 1) % 3;
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
