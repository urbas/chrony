package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.util.TimeUtils;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;

public class SingleDailyRecurrenceFinder implements RecurrenceFinder {

  private final List<Recurrence> foundRecurrences;

  public SingleDailyRecurrenceFinder(List<EventSample> eventSamples) {
    if (eventSamples.size() < 2) {
      foundRecurrences = emptyList();
    } else {
      long firstEventTimestamp = eventSamples.get(0).getTimestamp();
      long secondEventTimestamp = eventSamples.get(1).getTimestamp();
      int periodInDays = (int) Math.round((double) (secondEventTimestamp - firstEventTimestamp) / TimeUtils.DAY_IN_MILLIS);
      foundRecurrences = Arrays.<Recurrence>asList(
        new DailyPeriodRecurrence(
          periodInDays,
          firstEventTimestamp)
      );
    }
  }

  @Override
  public List<Recurrence> foundRecurrences() {
    return foundRecurrences;
  }
}
