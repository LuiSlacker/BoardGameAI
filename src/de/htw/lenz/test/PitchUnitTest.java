//package de.htw.lenz.test;
//
//import static org.junit.Assert.assertEquals;
//
//import java.awt.Point;
//import java.util.Arrays;
//
//import org.junit.Test;
//
//import de.htw.lenz.main.Pitch;
//import lenz.htw.bogapr.Move;
//
//public class PitchUnitTest {
//  
//  private static final int PLAYER1 = 0;
//  private static final int PLAYER2 = 1;
//  private static final int PLAYER3 = 2;
//
//  @Test
//  public void possibleFieldsToMoveToForOneChipTest_Step1() {
//    int positionInStack = 0;
//    Pitch pitch = new Pitch();
//    assertEquals(Arrays.asList(), pitch.getPossibleMovesForOneChip(1, positionInStack));
//    assertEquals(Arrays.asList(4), pitch.getPossibleMovesForOneChip(0, positionInStack));
//    assertEquals(Arrays.asList(4, 6, 11), pitch.getPossibleMovesForOneChip(5, positionInStack));
//    assertEquals(Arrays.asList(3, 5), pitch.getPossibleMovesForOneChip(4, positionInStack));
//  }
//  
//  @Test
//  public void possibleFieldsToMoveToForOneChipTest_Step2() {
//    int positionInStack = 1;
//    Pitch pitch = new Pitch();
//    assertEquals(Arrays.asList(3, 7, 10, 12), pitch.getPossibleMovesForOneChip(5, positionInStack));
//    assertEquals(Arrays.asList(4, 6), pitch.getPossibleMovesForOneChip(1, positionInStack));
//  }
//  
//  @Test
//  public void possibleFieldsToMoveToForOneChipTest_Step3() {
//    int positionInStack = 2;
//    Pitch pitch = new Pitch();
//    assertEquals(Arrays.asList(9, 13, 18, 20), pitch.getPossibleMovesForOneChip(5, positionInStack));
//    assertEquals(Arrays.asList(3, 5, 7), pitch.getPossibleMovesForOneChip(1, positionInStack));
//  }
//  
//  @Test
//  public void mapIndexToCoordinatesTest(){
//    assertEquals(2, Pitch.mapIndexToCoordinates(5).x);
//    assertEquals(2, Pitch.mapIndexToCoordinates(2).x);
//    assertEquals(0, Pitch.mapIndexToCoordinates(8).x);
//    
//    assertEquals(2, Pitch.mapIndexToCoordinates(5).y);
//    assertEquals(1, Pitch.mapIndexToCoordinates(2).y);
//    assertEquals(3, Pitch.mapIndexToCoordinates(8).y);
//    
//  }
//  
//  @Test
//  public void getRelativeRowTest(){
//    Pitch pitch = new Pitch();
//    
//    assertEquals(1, pitch.getRelativeRow(new Point(2, 1), PLAYER1));
//    assertEquals(6, pitch.getRelativeRow(new Point(2, 6), PLAYER1));
//    assertEquals(4, pitch.getRelativeRow(new Point(2, 4), PLAYER1));
//    
//    assertEquals(5, pitch.getRelativeRow(new Point(2, 2), PLAYER2));
//    assertEquals(1, pitch.getRelativeRow(new Point(1, 6), PLAYER2));
//    assertEquals(6, pitch.getRelativeRow(new Point(2, 1), PLAYER2));
//    
//    assertEquals(1, pitch.getRelativeRow(new Point(11, 6), PLAYER3));
//    assertEquals(5, pitch.getRelativeRow(new Point(2, 2), PLAYER3));
//    assertEquals(6, pitch.getRelativeRow(new Point(1, 6), PLAYER3));
//  }
//  
//  @Test
//  public void isWinningFieldTest() {
//    Pitch pitch = new Pitch();
//    assertEquals(true, pitch.isWinningField(new Point(2, 6), PLAYER1));
//    assertEquals(false, pitch.isWinningField(new Point(3, 6), PLAYER1));
//    
//    assertEquals(true, pitch.isWinningField(new Point(6, 3), PLAYER2));
//    assertEquals(true, pitch.isWinningField(new Point(2, 1), PLAYER2));
//    assertEquals(true, pitch.isWinningField(new Point(10, 5), PLAYER2));
//    assertEquals(false, pitch.isWinningField(new Point(7, 4), PLAYER2));
//    
//    assertEquals(true, pitch.isWinningField(new Point(0, 5), PLAYER3));
//    assertEquals(true, pitch.isWinningField(new Point(0, 1), PLAYER3));
//    assertEquals(false, pitch.isWinningField(new Point(1, 6), PLAYER3));
//    assertEquals(false, pitch.isWinningField(new Point(1, 2), PLAYER3));
//    assertEquals(false, pitch.isWinningField(new Point(1, 2), PLAYER3));
//    assertEquals(true, pitch.isWinningField(new Point(0, 3), PLAYER3));
//  }
//  
//  @Test
//  public void assessConfigurationTest() {
//    Pitch pitch = new Pitch();
//    assertEquals(90, pitch.assessConfiguration(PLAYER1), 0);
//    pitch.moveChip(new Move(0, 1, 3, 3));
//    assertEquals(110, pitch.assessConfiguration(PLAYER1), 0);
//  }
//
//}
