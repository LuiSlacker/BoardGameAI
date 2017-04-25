package de.htw.lenz.main;

import lenz.htw.bogapr.Move;

/**
 * Pitch representation with a bunch of utility methods for mutation
 *
 */
public class Pitch {
  
  private int[][] pitch = new int[47][3];
  private static final int INVALID = Integer.MIN_VALUE;
  private static final int PLAYER1 = 1;
  private static final int PLAYER2 = 2;
  private static final int PLAYER3 = 3;

  public Pitch() {
    initializePitch();
  }
  
  private void initializePitch() {
    
    // invalidate top lefthand corner
    this.pitch[35][0] = INVALID;
    this.pitch[35][1] = INVALID;
    this.pitch[35][2] = INVALID;
    
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
  
  private void intializeSingleFieldWithPlayer(int index, int player){
    for (int i = 0; i < 3; i++) {
        this.pitch[index][i] = player;
    }
  }
  
  private static int mapCoordinatesToIndex(int x, int y) {
      return x + rowOffset(y);
  }
  
  private static int rowOffset(int rowNumber) {
      rowNumber--;
      return (rowNumber + 2) * rowNumber;
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
  
  
}
