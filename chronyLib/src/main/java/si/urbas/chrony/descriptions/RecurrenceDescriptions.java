package si.urbas.chrony.descriptions;

import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.List;

import static si.urbas.chrony.util.Ordinals.toNumericOrdinal;

public class RecurrenceDescriptions {
  static final String DESCRIPTION_OF_EMPTY_RECURRENCE_ANALYSIS = "No recurrence pattern discovered.";

  public static String toShortDescriptionOf(List<? extends Recurrence> recurrenceAnalysis) {
    if (recurrenceAnalysis.size() == 1) {
      Recurrence recurrence = recurrenceAnalysis.get(0);
      return toShortDescriptionOf(recurrence);
    } else if (recurrenceAnalysis.size() > 1) {
      throw new UnsupportedOperationException();
    } else {
      return DESCRIPTION_OF_EMPTY_RECURRENCE_ANALYSIS;
    }
  }

  public static String toShortDescriptionOf(Recurrence recurrence) {
    if (recurrence instanceof DailyPeriodRecurrence) {
      DailyPeriodRecurrence dailyPeriodRecurrence = (DailyPeriodRecurrence) recurrence;
      return "every " + toOrdinalDay(dailyPeriodRecurrence) + " at " + formatTime(dailyPeriodRecurrence);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  private static String toOrdinalDay(DailyPeriodRecurrence recurrence) {
    int periodInDays = recurrence.getPeriodInDays();
    if (periodInDays > 1) {
      return toNumericOrdinal(periodInDays) + " day";
    } else {
      return "day";
    }
  }

  private static String formatTime(DailyPeriodRecurrence recurrence) {
    return recurrence.getHourOfDay() + ":" + recurrence.getMinutesPastHour();
  }
}
