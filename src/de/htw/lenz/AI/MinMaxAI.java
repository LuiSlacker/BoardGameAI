package de.htw.lenz.AI;

import java.util.List;

import de.htw.lenz.main.DynamicPlayerEnum;
import de.htw.lenz.main.Pitch;
import lenz.htw.bogapr.Move;

public class MinMaxAI implements GameAI{

  private Pitch pitch;
  private int maximizingPlayer;
  private DynamicPlayerEnum players;
  private Move currentlyWisestMove;
  
  private static int INITIAL_DEPTH = 4;
  
  public MinMaxAI() {
    this.players = new DynamicPlayerEnum();
  }
  
  @Override
  public void start() {
    int depth = INITIAL_DEPTH;
    while(true) {
      getWisestMove(depth);
      depth += 1;
    }
  }
  
  public void getWisestMove(int depth){
    miniMax(maximizingPlayer, depth, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }
  
  private double miniMax(int player, int originalDepth, int depth, double alpha, double beta) {
    System.out.println(player);
    if (player == maximizingPlayer) {
      return max(player, originalDepth, depth, alpha, beta);
    } else {
      return min(player, originalDepth, depth, alpha, beta);
    }
  }
  
  private double max(int player, int originalDepth, int depth, double alpha, double beta) {
    System.out.println("max");
    List<Move> possibleMoves = this.pitch.getPossibleMoves(player);
    if (depth == 0 || possibleMoves.isEmpty()) {
      //System.out.printf("player: %s, depth: %s\n", player, originalDepth);
      return this.pitch.assessConfiguration(maximizingPlayer);
    }
    double bestValue = alpha;
    for (Move move : possibleMoves) {
      this.pitch.moveChip(move);
      double childValue = miniMax(this.players.getNextPlayer(player), originalDepth, depth - 1, bestValue, beta);
      //if (depth == originalDepth) System.out.printf("%s, value %s\n", move, childValue);
      this.pitch.moveChipBack(move);
      if (childValue > bestValue) {
        bestValue = childValue;
        if (bestValue >= beta) break;
        if (depth == originalDepth) this.currentlyWisestMove = move;
      }
    }
    //if (depth == originalDepth) System.out.printf("move taken: %s", this.currentlyWisestMove);
    return bestValue;
  }
  
  private double min(int player, int originalDepth, int depth, double alpha, double beta) {
    System.out.println("min");
    List<Move> possibleMoves = this.pitch.getPossibleMoves(player);
    if (depth == 0 || possibleMoves.isEmpty()) {
      //System.out.printf("player: %s, depth: %s\n", player, originalDepth);
      return this.pitch.assessConfiguration(maximizingPlayer);
    }
    double bestValue = beta;
    for (Move move : possibleMoves) {
      this.pitch.moveChip(move);
      double childValue = miniMax(this.players.getNextPlayer(player), originalDepth, depth - 1, alpha, bestValue);
      this.pitch.moveChipBack(move);
      if (childValue < bestValue) {
        bestValue = childValue;
        if (bestValue <= alpha) break;
      }
    }
    return bestValue;
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
  
  @Override
  public void observePlayers(int player) {
    this.players.observePlayers(player);
  }

}
