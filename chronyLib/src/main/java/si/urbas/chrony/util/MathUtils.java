package si.urbas.chrony.util;

import static java.lang.Math.abs;

public class MathUtils {
  /**
   * @return the number whose absolute value is smallest.
   */
  public static long smallestByAbsoluteValue(long numberA, long numberB) {
    return abs(numberA) < abs(numberB) ? numberA : numberB;
  }
}
