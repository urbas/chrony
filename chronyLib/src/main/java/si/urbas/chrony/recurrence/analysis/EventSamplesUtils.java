package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;

import java.util.Calendar;
import java.util.Iterator;

import static si.urbas.chrony.util.TimeUtils.timeOfDayInMillis;

public class EventSamplesUtils {

  public static long averageTimeOfDay(Iterable<EventSample> eventSamples) {
    Iterator<EventSample> eventSampleIterator = eventSamples.iterator();
    if (!eventSampleIterator.hasNext()) {
      throw new IllegalArgumentException("No event samples given. Could not calculate the average time of day of occurrences.");
    }
    int count = 0;
    long averageTimeOfDayInMillis = 0;
    do {
      ++count;
      Calendar timestampAsCalendar = eventSampleIterator.next().getTimestampAsCalendar();
      averageTimeOfDayInMillis += timeOfDayInMillis(timestampAsCalendar);
    } while (eventSampleIterator.hasNext());
    return averageTimeOfDayInMillis / count;
  }

}
