package de.htw.lenz.AI;

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
  Logger logger;
  
  private static int INITIAL_DEPTH = 4;
  
  public MinMaxAI(Logger logger) {
    this.logger = logger;
    this.players = new DynamicPlayerEnum();
  }
  
  @Override
  public void start() {
    int depth = INITIAL_DEPTH;
//    while(true) {
      getWisestMove(depth);
      System.out.println("depth: " + depth + " done.");
//      logger.info("depth: " + depth + " done.");
      this.currentlyWisestMove = this.currentlyWisestMoveTemp;
      //depth++;
//    }
  }
  
  public void getWisestMove(int depth) {
//    logger.info("new search for player: " + maximizingPlayer + " - depth: " + depth + "----------------------------------------------");
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
//    logger.info("max");
    if (depth > 0) {
      List<Move> possibleMoves = this.pitch.getPossibleMoves(player);
      if (possibleMoves.isEmpty()) {
        return this.pitch.assessConfiguration(maximizingPlayer);
      } else {
//        logger.info("---");
        int bestValue = alpha;
        for (Move move : possibleMoves) {
//          logger.info("new for iteration");
//          logger.info(this.pitch.printScore2());
          this.pitch.moveChip(move);
//          logger.info("chip moved");
//          logger.info(this.pitch.printScore2());
          int childValue = miniMax(this.players.getNextPlayer(player), originalDepth, depth - 1, bestValue, beta);
//          logger.info("coming back up to max");
          this.pitch.moveChipBack(move);
//          logger.info("chip moved back");
//          logger.info(this.pitch.printScore2());
          if (childValue > bestValue) {
            bestValue = childValue;
            if (depth == originalDepth) {
//              logger.info("setting wisest move " + move + "for player" + player);
              this.currentlyWisestMoveTemp = move;
            }
            //if (bestValue >= beta) break;
          }
        }
//        String s = (depth == originalDepth) ? "bestValue taken for Move: " : "bestValue taken: "; 
//        logger.info(s + bestValue);
        return bestValue;
      }
    } else {
      int value = this.pitch.assessConfiguration(maximizingPlayer);
//      logger.info("leaf reached  - assessedValue: " + value);
      return value;
    }
  }
  
  private int min(int player, int originalDepth, int depth, int alpha, int beta) {
//    logger.info("min");
    if (depth > 0) {
      List<Move> possibleMoves = this.pitch.getPossibleMoves(player);
      if (possibleMoves.isEmpty()) {
        return this.pitch.assessConfiguration(maximizingPlayer);
      } else {
//        logger.info("---");
        int bestValue = beta;
        for (Move move : possibleMoves) {
//          logger.info("new for iteration");
//          logger.info(this.pitch.printScore2());
          this.pitch.moveChip(move);
//          logger.info("chip moved");
//          logger.info(this.pitch.printScore2());
          int childValue = miniMax(this.players.getNextPlayer(player), originalDepth, depth - 1, alpha, bestValue);
//          logger.info("coming back up to min");
          this.pitch.moveChipBack(move);
//          logger.info("chip moved back");
//          logger.info(this.pitch.printScore2());
          if (childValue < bestValue) {
            bestValue = childValue;
            //if (bestValue <= alpha) break;
          }
        }
        return bestValue;
      }
    } else {
      int value = this.pitch.assessConfiguration(maximizingPlayer);
//      logger.info("leaf reached  - assessedValue: " + value);
      return value;
    }
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
