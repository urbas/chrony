package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static si.urbas.chrony.util.EventSampleUtils.averagePeriod;
import static si.urbas.chrony.util.EventSampleUtils.averageTimeOfDay;
import static si.urbas.chrony.util.TimeUtils.withDateOnly;
import static si.urbas.chrony.util.TimeUtils.withTimeOfDay;

public class SingleDailyRecurrenceFinder implements RecurrenceFinder {

  private final List<Recurrence> foundRecurrences;

  public SingleDailyRecurrenceFinder(List<EventSample> eventSamples) {
    if (eventSamples.size() < 2) {
      foundRecurrences = emptyList();
    } else {
      foundRecurrences = Arrays.<Recurrence>asList(
        new DailyPeriodRecurrence(averagePeriod(eventSamples), firstOccurrenceWithAverageTimeOfDay(eventSamples))
      );
    }
  }

  public static long firstOccurrenceWithAverageTimeOfDay(List<EventSample> eventSamples) {
    return withTimeOfDay(
      withDateOnly(eventSamples.get(0).getTimestampAsCalendar()),
      averageTimeOfDay(eventSamples)
    ).getTimeInMillis();
  }

  @Override
  public List<Recurrence> foundRecurrences() {
    return foundRecurrences;
  }
}
