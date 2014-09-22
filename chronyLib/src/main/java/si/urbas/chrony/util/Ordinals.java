package si.urbas.chrony.util;

public class Ordinals {
  public static String toNumericOrdinal(int number) {
    if (number < 1) {
      throw new IllegalArgumentException("Given number must be positive.");
    }
    int lastTwoDigits = number % 100;
    if (lastTwoDigits > 20) {
      String lastDigitOrdinal = toIdealisedOrdinal(number % 10);
      return number + lastDigitOrdinal;
    } else {
      String idealisedOrdinal = toIdealisedOrdinal(number);
      return number + idealisedOrdinal;
    }
  }

  private static String toIdealisedOrdinal(int number) {
    switch (number) {
      case 1:
        return "st";
      case 2:
        return "nd";
      case 3:
        return "rd";
      default:
        return "th";
    }
  }
}
