package de.htw.lenz.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import de.htw.lenz.main.Pitch;

public class PitchUnitTest {

  @Test
  public void possibleFieldsToMoveToForOneChipTest_Step1() {
    Pitch pitch = new Pitch();
    assertEquals(Arrays.asList(0, 2), pitch.getPossibleMovesForOneChip(1, 0));
    assertEquals(Arrays.asList(1, 4), pitch.getPossibleMovesForOneChip(0, 0));
    assertEquals(Arrays.asList(4, 6, 11), pitch.getPossibleMovesForOneChip(5, 0));
    assertEquals(Arrays.asList(3, 5, 0), pitch.getPossibleMovesForOneChip(4, 0));
  }
  
  @Test
  public void possibleFieldsToMoveToForOneChipTest_Step2() {
    Pitch pitch = new Pitch();
    assertEquals(Arrays.asList(3, 0, 7, 2, 10, 12), pitch.getPossibleMovesForOneChip(5, 1));
    assertEquals(Arrays.asList(4, 6), pitch.getPossibleMovesForOneChip(1, 1));
  }
  
//  @Test
//  public void possibleFieldsToMoveToForOneChipTest_Step3() {
//    Pitch pitch = new Pitch();
//    assertEquals(Arrays.asList(3, 0, 7, 2, 10, 12), pitch.getPossibleMovesForOneChip(5, 1));
//    assertEquals(Arrays.asList(4, 6), pitch.getPossibleMovesForOneChip(1, 1));
//  }
  
  
  @Test
  public void mapIndexToCoordinatesTest(){
    assertEquals(2, Pitch.mapIndexToCoordinates(5).y);
    assertEquals(1, Pitch.mapIndexToCoordinates(2).y);
    assertEquals(3, Pitch.mapIndexToCoordinates(8).y);
  }

}
