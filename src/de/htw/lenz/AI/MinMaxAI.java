package de.htw.lenz.AI;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import de.htw.lenz.main.CustomFormatter;
import de.htw.lenz.main.DynamicPlayerEnum;
import de.htw.lenz.main.Pitch;
import lenz.htw.bogapr.Move;

public class MinMaxAI implements GameAI{

  private Pitch pitch;
  private int maximizingPlayer;
  private DynamicPlayerEnum players;
  private Move currentlyWisestMove;
  Logger logger;
  
  private static int INITIAL_DEPTH = 1;
  
  public MinMaxAI() {
    this.players = new DynamicPlayerEnum();
    logger = Logger.getLogger("MyLog");
    FileHandler fh;
    try {
      // This block configure the logger with handler and formatter
      fh = new FileHandler("/Users/petulantslacker/Desktop/log.log");
      logger.addHandler(fh);
      //System.setProperty("java.util.logging.SimpleFormatter.format",  "%1$tF %1$tT %4$s %2$s %5$s%6$s%n");
      SimpleFormatter formatter = new CustomFormatter();
      fh.setFormatter(formatter);
       
      // the following statement is used to log any messages
      logger.info("Logger initialized!");
       
    } catch (SecurityException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
  }
  
  @Override
  public void start() {
    int depth = INITIAL_DEPTH;
    while(true) {
      getWisestMove(depth);
      System.out.println("depth" + depth + " done.");
      depth += 1;
    }
  }
  
  public void getWisestMove(int depth){
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
    List<Move> possibleMoves = this.pitch.getPossibleMoves(player);
    if (depth == 0 || possibleMoves.isEmpty()) {
      return this.pitch.assessConfiguration(maximizingPlayer);
    } else {
      int bestValue = alpha;
      for (Move move : possibleMoves) {
        this.pitch.moveChip(move);
        int childValue = miniMax(this.players.getNextPlayer(player), originalDepth, depth - 1, bestValue, beta);
        this.pitch.moveChipBack(move);
        if (childValue > bestValue) {
          bestValue = childValue;
          if (depth == originalDepth) this.currentlyWisestMove = move;
          if (bestValue >= beta) break;
        }
      }
      return bestValue;
    }
  }
  
  private int min(int player, int originalDepth, int depth, int alpha, int beta) {
    List<Move> possibleMoves = this.pitch.getPossibleMoves(player);
    if (depth == 0 || possibleMoves.isEmpty()) {
      return this.pitch.assessConfiguration(maximizingPlayer);
    } else {
      int bestValue = beta;
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
