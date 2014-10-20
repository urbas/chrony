package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.util.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class GuessingRecurrenceFinder implements RecurrenceFinder {

  private static final int[] POSSIBLE_PERIODS = new int[]{1, 2, 3, 4, 5, 6, 7};
  private final List<Recurrence> foundRecurrences;

  public GuessingRecurrenceFinder(List<EventSample> eventSamples) {
    if (eventSamples.size() > 1) {
      foundRecurrences = guessPossibleRecurrences(eventSamples);
    } else {
      foundRecurrences = Collections.emptyList();
    }
  }

  @Override
  public List<Recurrence> foundRecurrences() {
    return foundRecurrences;
  }

  static List<Recurrence> guessPossibleRecurrences(List<EventSample> eventSamples) {
    List<Recurrence> guessedRecurrences = new ArrayList<Recurrence>();
    boolean[] halfHourIntervalsWithSamples = findHalfHourIntervalsWithSamples(eventSamples);
    int[] possiblePeriodsInDays = POSSIBLE_PERIODS;
    Calendar timeOfOccurrence = eventSamples.get(0).getTimestampAsCalendar();
    for (int i = 0; i < halfHourIntervalsWithSamples.length; i++) {
      if (halfHourIntervalsWithSamples[i]) {
        for (int periodInDays : possiblePeriodsInDays) {
          for (int dayOfWeek = 1; dayOfWeek <= 7; dayOfWeek++) {
            timeOfOccurrence.set(Calendar.DAY_OF_WEEK, dayOfWeek);
            timeOfOccurrence.set(Calendar.HOUR_OF_DAY, i / 2);
            timeOfOccurrence.set(Calendar.MINUTE, 30 * (i % 2));
            guessedRecurrences.add(new DailyPeriodRecurrence(periodInDays, timeOfOccurrence.getTimeInMillis()));
          }
        }
      }
    }
    return guessedRecurrences;
  }

  private static boolean[] findHalfHourIntervalsWithSamples(List<EventSample> eventSamples) {
    boolean[] halfHourIntervals = new boolean[TimeUtils.DAY_IN_HOURS * 2];
    for (EventSample eventSample : eventSamples) {
      Calendar timestamp = eventSample.getTimestampAsCalendar();
      int minute = timestamp.get(Calendar.MINUTE);
      int minuteSlot = minute / 30;
      int hour = timestamp.get(Calendar.HOUR_OF_DAY);
      int intervalIndex = hour * 2 + minuteSlot;
      halfHourIntervals[intervalIndex] = true;
    }
    return halfHourIntervals;
  }
}