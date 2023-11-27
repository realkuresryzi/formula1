package cz.muni.pa165.race.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility for points per position.
 */
public class PointsUtil {

  /**
   * Map with position and corresponding points.
   */
  public Map<Integer, Integer> points;

  /**
   * Constructor.
   */
  public PointsUtil() {
    points = new HashMap<>();
    points.put(1, 25);
    points.put(2, 18);
    points.put(3, 15);
    points.put(4, 12);
    points.put(5, 10);
    points.put(6, 8);
    points.put(7, 6);
    points.put(8, 4);
    points.put(9, 2);
    points.put(10, 1);
    for (int i = 11; i < 21; i++) {
      points.put(i, 0);
    }
  }
}
