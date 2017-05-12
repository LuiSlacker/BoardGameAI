package de.htw.lenz.main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lenz.htw.bogapr.Move;

/**
 * This class represents the pitch of the board game
 * It is able to track player moves to keep an up to date version of the pitch
 * and generate possible moves for a given player to a given time
 */
public class Pitch implements Cloneable{
  
  private int[][] pitch = new int[47][3];
  private int[] score = new int[3];
  
  private static final int INVALID_FIELD = 35;
  private static final int INVALID = Integer.MIN_VALUE;
  private static final int EMPTY = Integer.MAX_VALUE;
  private static final int PLAYER1 = 0;
  private static final int PLAYER2 = 1;
  private static final int PLAYER3 = 2;

  public Pitch() {
    initializePitch();
  }
  
  /**
   * Deep-clones an instance of this class
   * 
   * @return a deep-cloned instance of the Pitch-class
   */
  public Pitch clone() throws CloneNotSupportedException {
    Pitch clonedPitch = (Pitch) super.clone();
    clonedPitch.pitch = Utils.deepCopyMatrix2D(this.pitch);
    clonedPitch.score = Arrays.copyOf(this.score, this.score.length);
    return clonedPitch;
  }
  
  private void initializePitch() {
    
    initializeEmptyPitch();
    
    // invalidate top lefthand corner
    intializeSingleField(INVALID_FIELD, INVALID);
    
    // set initial player chips
    intializeSingleField(mapCoordinatesToIndex(0,1), PLAYER1);
    intializeSingleField(mapCoordinatesToIndex(1,1), PLAYER1);
    intializeSingleField(mapCoordinatesToIndex(2,1), PLAYER1);
    
    intializeSingleField(mapCoordinatesToIndex(1,6), PLAYER2);
    intializeSingleField(mapCoordinatesToIndex(2,6), PLAYER2);
    intializeSingleField(mapCoordinatesToIndex(0,5), PLAYER2);
    
    intializeSingleField(mapCoordinatesToIndex(10,6), PLAYER3);
    intializeSingleField(mapCoordinatesToIndex(11,6), PLAYER3);
    intializeSingleField(mapCoordinatesToIndex(10,5), PLAYER3);
  }
  
  /**
   * Initializes the pitch
   */
  private void initializeEmptyPitch() {
    for (int i = 0; i < pitch.length; i++) {
      for (int j = 0; j <= 2; j++) {
        this.pitch[i][j] = EMPTY;
      }
    }
  }
  
  /**
   * Initialized as given field with a given value
   * 
   * @param index the index of the field to be initialized
   * @param player the player to initialize the field with
   */
  private void intializeSingleField(int index, int value) {
    for (int i = 0; i < 3; i++) {
        this.pitch[index][i] = value;
    }
  }
  
  /**
   * Handles one move of a chip
   * No validation of move necessary since server only sends valid moves
   * 
   * @param move the move to be taken care of
   */
  public void moveChip(Move move) {
      int fromIndex  = mapCoordinatesToIndex(move.fromX, move.fromY);
      int toIndex  = mapCoordinatesToIndex(move.toX, move.toY);
      int player = takeChip(fromIndex, false);
      setChip(toIndex, player);
  }
  
  /**
   * Undoes a given move and decrements score if applicable
   * This method is only called by the AI Algorithm
   * 
   * @param move the Move to be undone
   */
  public void moveChipBack(Move move) {
    int fromIndex  = mapCoordinatesToIndex(move.toX, move.toY);
    int toIndex  = mapCoordinatesToIndex(move.fromX, move.fromY);
    int player = takeChip(fromIndex, true);
    setChip(toIndex, player);
  }
  
  /**
   * Removes the chip which is on top of the "stack" of a given field and returns the respective player
   * 
   * @param index index of the field (internal array representation) to take the chip from
   * @return the player of the top most chip of the "stack" of that field
   */
  private int takeChip(int index, boolean AIMode) {
    int player = INVALID;
    for (int i = 2; i >= 0; i--) {
      if (this.pitch[index][i] != EMPTY) {
        player = this.pitch[index][i];
        this.pitch[index][i] = EMPTY;
        if (AIMode && i > 0 && this.pitch[index][i-1] != player) decrementScore(player);
        break;
      };
    }
    return player;
  }
  
  /**
   * Sets a chip of a given player on top of the "stack" of a field
   * and updates the score if the chip is placed on top of another player
   * 
   * @param index index of the field (internal array representation) to set the chip
   * @param player player to set the chip for
   */
  private void setChip(int index, int player) {
    for (int i = 0; i <= 2; i++) {
      if (this.pitch[index][i] == EMPTY) {
        this.pitch[index][i] = player;
        if (i > 0 && this.pitch[index][i-1] != player) incrementScore(player);
        break;
      }
    }
  }
  
  /**
   * Increments the score for a given player by 1
   * 
   * @param player the player to increment the score for
   */
  private void incrementScore(int player) {
    if (player > 0 && player < 3) this.score[player] += 1;
  }
  
  /**
   * Decrements the score for a given player by 1
   * 
   * @param player the player to decrement the score for
   */
  private void decrementScore(int player) {
    this.score[player] -= 1;
  }
  
  /**
   * Returns the score for a given player
   * 
   * @param player the player to return the score for
   * @return the score for the given player
   */
  private int getScoreForPlayer(int player) {
    return this.score[player];
  }
  
  /**
   * Returns a random valid Move for a given player
   * 
   * @param player the player to generate the random Move for
   * @return a random Move
   */
  public Move getRandomMove(int player) {
    List<Move> possibleMoves = getPossibleMoves(player);
    return possibleMoves.get((int)(Math.random() * possibleMoves.size()));
  }
  
  
  /**
   * Generates a List of possible Moves for a given player
   * 
   * @param player the player to generate the list for
   * @return a List<Moves> containing all possible Moves for a given player to a given time
   */
  public List<Move> getPossibleMoves(int player) {
    List<Move> possibleMoves = new ArrayList<>();
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
  
  /**
   * Wrapper for the recursive function - possibleFieldsToMoveToForOneChip
   * 
   * @param index the index to start looking from
   * @param positioninStack position in fields stack
   * @return a list of possible fields to move to
   */
  public List<Integer> getPossibleMovesForOneChip(int index, int positioninStack) {
    return possibleFieldsToMoveToForOneChip(index, positioninStack + 1, new ArrayList<Integer>(), new HashSet<Integer>());
  }
  
  /**
   * Recursively checks for possible fields to move to and returns a list of those
   * 
   * @param index the index to start looking from
   * @param stepSize the number of steps for the move (depends on the position within fields stack)
   * @param possibleFields the accumulation of possibles moves
   * @param fieldsVisited Set to overcome the issue of repeatedly visited fields 
   * @return a list of possible fields to move to
   */
  private List<Integer> possibleFieldsToMoveToForOneChip(int index, int stepSize, List<Integer> possibleFields, Set<Integer> fieldsVisited) {
    if (stepSize == 0) {
      possibleFields.add(index);
    } else {
      int rowNr = mapIndexToCoordinates(index).y;
      fieldsVisited.add(index);
      
      int moveLeft = index - 1;
      int moveRight = index + 1;
      int moveTopOrBottom = getTopOrBottomField(index, rowNr);
      
      // only call recursively if:
      //    * next field is valid
      //    * was not yet visited
      //    * is within same row (only checked for left and right recursive calls)
      if (isValidField(moveLeft) && !fieldsVisited.contains(moveLeft) && isFieldWithinSameRow(rowNr, moveLeft)) 
        possibleFieldsToMoveToForOneChip(moveLeft, stepSize - 1, possibleFields, fieldsVisited);
      if (isValidField(moveRight) && !fieldsVisited.contains(moveRight) && isFieldWithinSameRow(rowNr, moveRight))
        possibleFieldsToMoveToForOneChip(moveRight, stepSize - 1, possibleFields, fieldsVisited);
      if (isValidField(moveTopOrBottom) && !fieldsVisited.contains(moveTopOrBottom)) 
        possibleFieldsToMoveToForOneChip(moveTopOrBottom, stepSize - 1, possibleFields, fieldsVisited);
    }
    return possibleFields;
  }
  
  /**
   * Returns the field to the top or bottom of a given field
   * @param index
   * @param rowNr
   * @return
   */
  private int getTopOrBottomField(int index, int rowNr) {
    int moveTopOrBottom;
    boolean evenRowNr = rowNr % 2 == 0;
    if (evenRowNr) {
      moveTopOrBottom = (index % 2 == 0) ? getFieldToTheBottom(index, rowNr) : getFieldToTheTop(index, rowNr);
    } else {
      moveTopOrBottom = (index % 2 == 0) ? getFieldToTheTop(index, rowNr) : getFieldToTheBottom(index, rowNr);
    }
    return moveTopOrBottom;
  }
  
  /**
   * Checks whether index is pointing to a valid field of the pitch
   * The checks for the field include:
   *    * if it is within the pitch's range
   *    * if it is not the excluded field (top - left)
   *    * if it is not fully occupied
   * 
   * @param index the index of the field to be checked
   * @return boolean whether index is valid
   */
  private boolean isValidField(int index) {
    return index >= 0 && index < this.pitch.length && index != INVALID_FIELD && this.pitch[index][2] == EMPTY;
  }
  
  /**
   * Returns a List of movable chips for one player
   * 
   * @param player the player to get the movable chips for
   * @return a list of movable chips for one player
   *         The list contains a Point whereby x is the index of the field and y the position within that fields stack 
   */
  private List<Point> findMovablePlayerChips(int player) {
    List<Point> possibleMoves = new ArrayList<>();
    for (int i = 0; i < pitch.length; i++) {
      if(pitch[i][0] == INVALID) continue; // skip invalid field
      innerloop:
      for (int j = 2; j >= 0; j--) {
        if (this.pitch[i][j] == player) {
          possibleMoves.add(new Point(i,j));
          break innerloop;
        } else if (this.pitch[i][j] != EMPTY) break innerloop;
      }
    }
    return possibleMoves;
  }
  
  
  /***************************************************************************************************************
   ** STATIC METHODS
   **************************************************************************************************************/
  
  /**
   * Maps coordinates to the pitch-representing array index
   * @param x x-coordinate
   * @param y y-coordinate
   * @return the respective index
   */
  public static int mapCoordinatesToIndex(int x, int y) {
      return (int) (x + Math.pow(y, 2) - 1);
  }

  /**
   * Maps index to coordinates
   * @param index the index to be transformed
   * @return a Point representing the coordinates
   */
  public static Point mapIndexToCoordinates(int index) {
    int x = index;
    int y = 1;
    for (int i = 5; i > 0; i--) {
      int rest =  index - (i * (i + 2));
      if (rest >= 0){
        x = rest;
        y = i+1;
        break;
      }
    }
    return new Point(x, y);
  }
  
  /**
   * Checks if a given field is within the same row as the given rowNr
   * @param rowNr the number of the row to be checked against
   * @param newIndex the index to be checked
   * @return
   */
  private static boolean isFieldWithinSameRow(int rowNr, int newIndex) {
    return rowNr == mapIndexToCoordinates(newIndex).y;
  }
  
  /**
   * Returns the index of the field to the top
   * @param index
   * @param columnNr
   * @return
   */
  private static int getFieldToTheTop(int index, int columnNr) {
    return index + (2 * columnNr) + 2;
  }
  
  /**
   * Returns the index of the field to the bottom
   * 
   * @param index
   * @param columnNr
   * @return 
   */
  private static int getFieldToTheBottom(int index, int columnNr) {
    return index - (2 * columnNr);
  }

  public double assessConfiguration(int player) {
    double value = getScoreForPlayer(player) * 5.0;;
    value += evaluateChipPositions(player);
    return value;
  }
  
  private double evaluateChipPositions(int player) {
    double value = 0.0;
    List<Point> allChips = findAllPlayersChips(player);
    for (int i = 0; i < allChips.size(); i++) {
      int index = allChips.get(i).x;
      Point coordinate = mapIndexToCoordinates(index);
      value += getRelativeRow(coordinate, player); // / 6.0;
      if (isWinningField(coordinate, player)) value += 5000.0;
    }
    return value;
  }
  
  /**
   * Returns all chips for a given player
   * 
   * @return all chips for a given player
   */
  private List<Point> findAllPlayersChips(int player) {
    List<Point> allChips = new ArrayList<>();
    for (int i = 0; i < pitch.length; i++) {
      for (int j = 0; j < 3; j++) {
        if (this.pitch[i][j] == player) {
          allChips.add(new Point(i, j));
        }
      }
    }
    return allChips;
  }
  
  /**
   * Returns the relative row(1-6) for a given player
   * 
   * @param coordinate the coordinate to return the relative row for
   * @param player the player to return the relative row for
   * @return the relative row for a given player
   */
  public int getRelativeRow(Point coordinate, int player){
    int row = 0;
    switch (player) {
    case PLAYER1:
      row = coordinate.y;
      break;
    case PLAYER2:
      row = 6 - coordinate.y + (coordinate.x / 2) + (coordinate.x % 2);
      break;
    case PLAYER3:
      row = 6 - (coordinate.x / 2);
      break;
    }
    return row;
  }
  
  private boolean isWinningField(Point coordinate, int player) {
    return getRelativeRow(coordinate, player) == 6 && coordinate.x % 2 == 0;
  }

}
