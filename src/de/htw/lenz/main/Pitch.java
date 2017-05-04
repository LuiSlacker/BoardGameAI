package de.htw.lenz.main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import lenz.htw.bogapr.Move;

/**
 * Pitch representation with a bunch of utility methods for mutation
 *
 */
public class Pitch {
  
  private int[][] pitch = new int[47][3];
  private static int invalidField = 35;
  private static final int INVALID = Integer.MIN_VALUE;
  private static final int PLAYER1 = 1;
  private static final int PLAYER2 = 2;
  private static final int PLAYER3 = 3;

  public Pitch() {
    initializePitch();
  }
  
  private void initializePitch() {
    
    // invalidate top lefthand corner
    this.pitch[invalidField][0] = INVALID;
    this.pitch[invalidField][1] = INVALID;
    this.pitch[invalidField][2] = INVALID;
    
    // set initial player chips
    intializeSingleFieldWithPlayer(mapCoordinatesToIndex(0,1), PLAYER1);
    intializeSingleFieldWithPlayer(mapCoordinatesToIndex(1,1), PLAYER1);
    intializeSingleFieldWithPlayer(mapCoordinatesToIndex(2,1), PLAYER1);
    
    intializeSingleFieldWithPlayer(mapCoordinatesToIndex(1,6), PLAYER2);
    intializeSingleFieldWithPlayer(mapCoordinatesToIndex(2,6), PLAYER2);
    intializeSingleFieldWithPlayer(mapCoordinatesToIndex(0,5), PLAYER2);
    
    intializeSingleFieldWithPlayer(mapCoordinatesToIndex(10,6), PLAYER3);
    intializeSingleFieldWithPlayer(mapCoordinatesToIndex(11,6), PLAYER3);
    intializeSingleFieldWithPlayer(mapCoordinatesToIndex(10,5), PLAYER3);
  }
  
  /**
   * pitch initialization helper
   * @param index
   * @param player
   */
  private void intializeSingleFieldWithPlayer(int index, int player){
    for (int i = 0; i < 3; i++) {
        this.pitch[index][i] = player;
    }
  }
  
  private static int mapCoordinatesToIndex(int x, int y) {
      return (int) (x + Math.pow(y, 2) - 1);
  }
  
//  private static int rowOffset(int rowNumber) {
//      rowNumber--;
//      return (rowNumber + 2) * rowNumber;
//  }
  
  private static Point mapIndexToCoordinates(int index) {
    int x = index;
    int y = 1;
    for (int i = 6; i > 2; i--) {
      int rest = (int) (index - Math.pow(i, 2));
      if (rest >= 0){
        x = rest;
        y = i+1;
        break;
      }
    }
    return new Point(x, y);
  }
  
  /**
   * handles one move of a chip
   * NOTE: No validation of move necessary since server only sends valid moves
   * 
   * @param move the move to be taken care of
   */
  public void moveChip(Move move) {
      int fromIndex  = mapCoordinatesToIndex(move.fromX, move.fromY);
      int toIndex  = mapCoordinatesToIndex(move.toX, move.toY);
      int player = takeChip(fromIndex);
      setChip(toIndex, player);
  }
  
  /**
   * removes the chip which is on top of the "stack" of a field and returns the corresponding player
   * 
   * @param index index of the field (internal array representation) to take the chip from
   * @return the player of the top most chip of the "stack" of that field
   */
  private int takeChip(int index) {
      int player = INVALID;
      for (int i = 2; i >= 0; i--) {
          if (this.pitch[index][i] != 0) {
              player = this.pitch[index][i];
              this.pitch[index][i] = 0;
              break;
          };
      }
      return player;
  }
  
  /**
   * sets a chip of player on top of the "stack" of a field
   *  
   * @param index index of the field (internal array representation) to set the chip
   * @param player player to set the chip for
   */
  private void setChip(int index, int player) {
      for (int i = 0; i <= 2; i++) {
          if (this.pitch[index][i] == 0) {
              this.pitch[index][i] = player;
              break;
          }
      }
  }
  
  public List<Move> getPossibleMoves(int player) {
    List<Move> possibleMoves = new ArrayList<Move>();
    List<Point> movablePlayerChips = findMovablePlayerChips(player);
    for (Point chip: movablePlayerChips) {
      Point fromCoordinate = mapIndexToCoordinates(chip.x);
      List<Integer> possibleMovesForOneChip = getPossibleMovesForOneChip(chip.x, chip.y);
      for (Integer possibleMove : possibleMovesForOneChip) {
        Point toCoordinate = mapIndexToCoordinates(possibleMove);
        possibleMoves.add(new Move(fromCoordinate.x, fromCoordinate.y, toCoordinate.x, toCoordinate.y));
      }
    }
    
    return possibleMoves;
  }
  
  private List<Integer> getPossibleMovesForOneChip(int index, int positioninStack) {
    List<Integer> moves = new ArrayList<Integer>();
    int stepSize = positioninStack + 1;
    
    int moveLeft = index - stepSize;
    int moveRight = index + stepSize;
    
    if (isValidField(moveLeft)) moves.add(moveLeft);
    if (isValidField(moveRight)) moves.add(moveRight);
    
    return moves;
  }
  
  
  
  private boolean isValidField(int index) {
    return index > 0 && index < this.pitch.length && index != invalidField;
  }
  
  /**
   * returns a List of movable chips for one player - so to speak: returns all chips that are on top of the stacks of all fields
   * @param player the player to get the movable chips for
   * @return a list of movable chips for one player  - the list contains a Point whereby x is the index of the field and y the position within that fields stack 
   */
  private List<Point> findMovablePlayerChips(int player) {
    List<Point> possibleMoves = new ArrayList<Point>();
    for (int i = 0; i < pitch.length; i++) {
      innerloop:
      for (int j = 2; j >= 0; j--) {
        if (this.pitch[i][j] == player) {
          possibleMoves.add(new Point(i,j));
          break innerloop;
        } else if (this.pitch[i][j] != 0) break innerloop;
      }
    }
    return possibleMoves;
  }
  
  
}
