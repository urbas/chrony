package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.ConstantPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.util.EventSampleUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    int[] timesOfDay = new TimeOfDayClusterer().millisOfDayClusters(eventSamples);
    long startTime = EventSampleUtils.oldestTimestamp(eventSamples).withMillisOfDay(timesOfDay[0]).getMillis();
    long endTime = EventSampleUtils.newestTimestamp(eventSamples).plusDays(1).getMillis();
    return Arrays.<Recurrence>asList(new ConstantPeriodRecurrence(startTime, 3 * DAY_IN_MILLIS, endTime));
  }

}