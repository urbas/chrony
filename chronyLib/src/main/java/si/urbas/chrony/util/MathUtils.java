package si.urbas.chrony.util;

import java.util.Random;

import static java.lang.Math.abs;

public class MathUtils {
  public static boolean isWithin(long number, long centerPoint, long radius) {
    return abs(number - centerPoint) <= radius;
  }

  /**
   * @return the number whose absolute value is smallest.
   */
  public static long smallestByAbsoluteValue(long numberA, long numberB) {
    return abs(numberA) < abs(numberB) ? numberA : numberB;
  }

  public static long randomValueBetween(Random randomnessSource, long rangeStart, long rangeEnd) {
    return Math.round(randomnessSource.nextDouble() * (rangeEnd - rangeStart) + rangeStart);
  }
}
