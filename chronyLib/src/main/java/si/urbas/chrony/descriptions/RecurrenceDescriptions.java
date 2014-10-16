package si.urbas.chrony.descriptions;

import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;

import java.text.SimpleDateFormat;
import java.util.List;

import static si.urbas.chrony.util.Ordinals.toNumericOrdinal;

public class RecurrenceDescriptions {

  static final String DESCRIPTION_OF_EMPTY_RECURRENCE_ANALYSIS = "No recurrence pattern discovered.";
  private static final SimpleDateFormat HOUR_AND_MINUTE_FORMAT = new SimpleDateFormat("HH:mm");

  public static String toShortDescription(List<Recurrence> recurrences) {
    if (recurrences.size() == 1) {
      return toShortDescription(recurrences.get(0));
    } else if (recurrences.size() > 1) {
      throw new UnsupportedOperationException();
    } else {
      return DESCRIPTION_OF_EMPTY_RECURRENCE_ANALYSIS;
    }
  }

  public static String toShortDescription(Recurrence recurrence) {
    if (recurrence instanceof DailyPeriodRecurrence) {
      return toShortDescription((DailyPeriodRecurrence) recurrence);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  private static String toShortDescription(DailyPeriodRecurrence dailyPeriodRecurrence) {
    return "every " + toOrdinalDay(dailyPeriodRecurrence) + " at " + formatTime(dailyPeriodRecurrence);
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
    return HOUR_AND_MINUTE_FORMAT.format(recurrence.getFirstOccurrence().getTime());
  }
}
