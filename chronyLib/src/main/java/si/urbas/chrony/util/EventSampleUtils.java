package si.urbas.chrony.util;

import si.urbas.chrony.EventSample;

import java.util.*;

import static si.urbas.chrony.util.TimeUtils.timeOfDayInMillis;

public class EventSampleUtils {

  public static int countSamplesWithinTime(Iterable<EventSample> eventSamples, long fromTime, long untilTime) {
    int count = 0;
    for (EventSample eventSample : eventSamples) {
      if (eventSample.getTimestamp() >= fromTime && eventSample.getTimestamp() <= untilTime) {
        count++;
      }
    }
    return count;
  }

  public static long getMinimumTimestamp(Collection<EventSample> eventSamples) {
    return Collections.min(eventSamples, EventSampleTimestampComparator.INSTANCE).getTimestamp();
  }

  public static long getMaximumTimestamp(Collection<EventSample> eventSamples) {
    return Collections.max(eventSamples, EventSampleTimestampComparator.INSTANCE).getTimestamp();
  }

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

  public static int averagePeriod(List<EventSample> eventSamples) {
    return averagePeriod(
      eventSamples.size(),
      eventSamples.get(0).getTimestamp(),
      eventSamples.get(eventSamples.size() - 1).getTimestamp()
    );
  }

  public static int averagePeriod(int numberOfOccurrences,
                                  long firstOccurrenceTimeInMillis,
                                  long lastOccurrenceTimeInMillis) {
    double durationInDays = (double) (lastOccurrenceTimeInMillis - firstOccurrenceTimeInMillis) / TimeUtils.DAY_IN_MILLIS;
    return (int) Math.round(durationInDays / (numberOfOccurrences - 1));
  }
}
