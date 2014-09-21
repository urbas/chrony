package si.urbas.chrony.descriptions;

import si.urbas.chrony.recurrence.Recurrence;

import java.util.List;

public class RecurrenceDescriptions {
  static final String DESCRIPTION_OF_EMPTY_RECURRENCE_ANALYSIS = "No recurrence pattern discovered.";

  public static String getShortDescriptionOf(List<? extends Recurrence> recurrenceAnalysis) {
    if (recurrenceAnalysis.size() == 1) {
      Recurrence recurrence = recurrenceAnalysis.get(0);
      return "at " + formatTime(recurrence) + "day";
    } else {
      return DESCRIPTION_OF_EMPTY_RECURRENCE_ANALYSIS;
    }
  }

  private static String formatTime(Recurrence recurrence) {
    return recurrence.getHourOfDay() + ":" + recurrence.getMinutesPastHour();
  }
}
