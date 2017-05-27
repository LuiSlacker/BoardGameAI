package de.htw.lenz.utils;

import java.util.Arrays;

public class Utils {
  
  /**
   * DeepClones a two dimensional Array
   * Method taken from http://stackoverflow.com/a/31637145/3254533
   * 
   * @param matrix the array to be deep cloned
   * @return a deep cloned two-dimensional array
   */
  public static int[][] deepCopyMatrix2D(int[][] matrix) {
    return Arrays.stream(matrix).map(el -> el.clone()).toArray($ -> matrix.clone());
}

}