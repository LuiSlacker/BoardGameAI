package de.htw.lenz.AI;

import java.awt.Point;
import java.util.Collections;
import java.util.List;

import de.htw.lenz.gameUtils.DynamicPlayerEnum;
import de.htw.lenz.gameUtils.Pitch;
import lenz.htw.bogapr.Move;

public class MinMaxAI implements GameAI{

  private Pitch pitch;
  private int maximizingPlayer;
  private DynamicPlayerEnum players;
  private Move currentlyWisestMoveTemp;
  private Move currentlyWisestMove;
  
  private static int INITIAL_DEPTH = 3;
  
  /**
   * Triggers the incremental search for a best move
   */
  @Override
  public void start() {
    int depth = INITIAL_DEPTH;
    while(true) {
      getWisestMove(depth);
      System.out.println("depth: " + depth + " done.");
      this.currentlyWisestMove = this.currentlyWisestMoveTemp;
      depth++;
    }
  }
  
  /**
   * Initial recursive call
   * @param depth the depth for the tree to be calculated
   */
  public void getWisestMove(int depth) {
    miniMax(maximizingPlayer, depth, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }
  
  /**
   * Recursive wrapper to call the corresponding function(min or max) for each player
   * @param player
   * @param originalDepth
   * @param depth
   * @param alpha
   * @param beta
   * @return
   */
  private int miniMax(int player, int originalDepth, int depth, int alpha, int beta) {
    if (player == maximizingPlayer) {
      return max(player, originalDepth, depth, alpha, beta);
    } else {
      return min(player, originalDepth, depth, alpha, beta);
    }
  }
  
  /**
   * Recursive function for the maximizing player
   * @param player
   * @param originalDepth
   * @param depth
   * @param alpha
   * @param beta
   * @return
   */
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
            if (bestValue >= beta
            || (Pitch.isWinningField(new Point(move.toX,  move.toY), maximizingPlayer) && this.pitch.wouldWinningFieldReallyWin(maximizingPlayer))) break;
          }
        }
        return bestValue;
      }
    } else return this.pitch.assessConfiguration(maximizingPlayer);
  }
  
  /**
   * Recursive function for the minimizing player
   * @param player
   * @param originalDepth
   * @param depth
   * @param alpha
   * @param beta
   * @return
   */
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
