package de.htw.lenz.AI;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import de.htw.lenz.main.DynamicPlayerEnum;
import de.htw.lenz.main.Pitch;
import lenz.htw.bogapr.Move;

public class MinMaxAI implements GameAI{

  private Pitch pitch;
  private int maximizingPlayer;
  private DynamicPlayerEnum players;
  private Move currentlyWisestMoveTemp;
  private Move currentlyWisestMove;
  
  private static int INITIAL_DEPTH = 3;
  
  @Override
  public void start() {
    int depth = INITIAL_DEPTH;
    while(true) {
      getWisestMove(depth);
//      System.out.println("depth: " + depth + " done.");
      this.currentlyWisestMove = this.currentlyWisestMoveTemp;
      depth++;
    }
  }
  
  public void getWisestMove(int depth) {
    miniMax(maximizingPlayer, depth, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }
  
  private int miniMax(int player, int originalDepth, int depth, int alpha, int beta) {
    if (player == maximizingPlayer) {
      return max(player, originalDepth, depth, alpha, beta);
    } else {
      return min(player, originalDepth, depth, alpha, beta);
    }
  }
  
  private int max(int player, int originalDepth, int depth, int alpha, int beta) {
    if (depth > 0) {
      List<Move> possibleMoves = this.pitch.getPossibleMoves(player);
      if (possibleMoves.isEmpty()) {
        return this.pitch.assessConfiguration(maximizingPlayer);
      } else {
        int bestValue = alpha;
        Collections.shuffle(possibleMoves);
        for (Move move : possibleMoves) {
          this.pitch.moveChip(move);
          int childValue = miniMax(this.players.getNextPlayer(player), originalDepth, depth - 1, bestValue, beta);
          this.pitch.moveChipBack(move);
          if (childValue > bestValue) {
            bestValue = childValue;
            if (depth == originalDepth) {
              this.currentlyWisestMoveTemp = move;
            }
            if (bestValue >= beta) break;
          }
        }
        return bestValue;
      }
    } else return this.pitch.assessConfiguration(maximizingPlayer);
  }
  
  private int min(int player, int originalDepth, int depth, int alpha, int beta) {
    if (depth > 0) {
      List<Move> possibleMoves = this.pitch.getPossibleMoves(player);
      if (possibleMoves.isEmpty()) {
        return this.pitch.assessConfiguration(maximizingPlayer);
      } else {
        int bestValue = beta;
        Collections.shuffle(possibleMoves);
        for (Move move : possibleMoves) {
          this.pitch.moveChip(move);
          int childValue = miniMax(this.players.getNextPlayer(player), originalDepth, depth - 1, alpha, bestValue);
          this.pitch.moveChipBack(move);
          if (childValue < bestValue) {
            bestValue = childValue;
            if (bestValue <= alpha) break;
          }
        }
        return bestValue;
      }
    } else return this.pitch.assessConfiguration(maximizingPlayer);
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
  public void setPlayers(DynamicPlayerEnum players) {
    this.players = players;
  }

}
