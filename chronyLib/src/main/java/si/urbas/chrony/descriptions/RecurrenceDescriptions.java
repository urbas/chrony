package si.urbas.chrony.descriptions;

import si.urbas.chrony.recurrence.Recurrence;

import java.util.List;

import static si.urbas.chrony.util.Ordinals.toNumericOrdinal;

public class RecurrenceDescriptions {
  static final String DESCRIPTION_OF_EMPTY_RECURRENCE_ANALYSIS = "No recurrence pattern discovered.";

  public static String toShortDescriptionOf(List<? extends Recurrence> recurrenceAnalysis) {
    if (recurrenceAnalysis.size() == 1) {
      Recurrence recurrence = recurrenceAnalysis.get(0);
      return "every " + toOrdinalDay(recurrence) + " at " + formatTime(recurrence);
    } else {
      return DESCRIPTION_OF_EMPTY_RECURRENCE_ANALYSIS;
    }
  }

  private static String toOrdinalDay(Recurrence recurrence) {
    int periodInDays = recurrence.getPeriodInDays();
    if (periodInDays > 1) {
      return toNumericOrdinal(periodInDays) + " day";
    } else {
      return "day";
    }
  }

  private static String formatTime(Recurrence recurrence) {
    return recurrence.getHourOfDay() + ":" + recurrence.getMinutesPastHour();
  }
}
