package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.util.EventSampleUtils;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static si.urbas.chrony.util.EventSampleUtils.averageTimeOfDay;

public class SingleDailyRecurrenceFinder implements RecurrenceFinder {

  private final List<Recurrence> foundRecurrences;

  public SingleDailyRecurrenceFinder(List<EventSample> eventSamples) {
    if (eventSamples.size() < 2) {
      foundRecurrences = emptyList();
    } else {
      foundRecurrences = Arrays.<Recurrence>asList(
        new DailyPeriodRecurrence(EventSampleUtils.averagePeriodInDays(eventSamples), firstOccurrenceWithAverageTimeOfDay(eventSamples))
      );
    }
  }

  public static org.joda.time.DateTime firstOccurrenceWithAverageTimeOfDay(List<EventSample> eventSamples) {
    return eventSamples.get(0).getTimestamp().withMillisOfDay(averageTimeOfDay(eventSamples));
  }

  @Override
  public List<Recurrence> foundRecurrences() {
    return foundRecurrences;
  }
}
