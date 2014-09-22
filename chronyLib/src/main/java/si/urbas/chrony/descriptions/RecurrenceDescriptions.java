package si.urbas.chrony.descriptions;

import si.urbas.chrony.recurrence.Recurrence;

import java.util.List;

import static si.urbas.chrony.util.Ordinals.toNumericOrdinal;

public class RecurrenceDescriptions {
  static final String DESCRIPTION_OF_EMPTY_RECURRENCE_ANALYSIS = "No recurrence pattern discovered.";

  public static String getShortDescriptionOf(List<? extends Recurrence> recurrenceAnalysis) {
    if (recurrenceAnalysis.size() == 1) {
      Recurrence recurrence = recurrenceAnalysis.get(0);
      String ordinal = toNumericOrdinal(recurrence.getPeriodInDays());
      return "at " + formatTime(recurrence) + " every"+ordinal+" day";
    } else {
      return DESCRIPTION_OF_EMPTY_RECURRENCE_ANALYSIS;
    }
  }

  private static String formatTime(Recurrence recurrence) {
    return recurrence.getHourOfDay() + ":" + recurrence.getMinutesPastHour();
  }
}
