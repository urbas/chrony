package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.ConstantPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.util.EventSampleUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static si.urbas.chrony.recurrence.analysis.TimeOfDayClusterer.millisOfDayClusters;
import static si.urbas.chrony.util.TimeUtils.DAY_IN_MILLIS;

public class GuessingRecurrenceFinder implements RecurrenceFinder {

  @Override
  public List<Recurrence> foundRecurrences(List<EventSample> eventSamples) {
    if (eventSamples.size() > 1) {
      return guessPossibleRecurrences(eventSamples);
    } else {
      return Collections.emptyList();
    }
  }

  private static List<Recurrence> guessPossibleRecurrences(List<EventSample> eventSamples) {
    ArrayList<Recurrence> recurrences = new ArrayList<Recurrence>();
    long endTime = EventSampleUtils.newestTimestamp(eventSamples).plusDays(1).getMillis();
    long periodInMillis = 3 * DAY_IN_MILLIS;
    for (int timeOfDay : millisOfDayClusters(eventSamples)) {
      long firstOccurrence = EventSampleUtils.oldestTimestamp(eventSamples).withMillisOfDay(timeOfDay).getMillis();
      recurrences.add(new ConstantPeriodRecurrence(firstOccurrence, periodInMillis, endTime));
    }
    return recurrences;
  }

}