package si.urbas.chrony.util;

import org.joda.time.DateTime;
import si.urbas.chrony.EventSample;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class EventSampleUtils {

  public static List<EventSample> sort(List<EventSample> eventSamples) {
    Collections.sort(eventSamples, EventSampleTimestampComparator.INSTANCE);
    return eventSamples;
  }

  public static int countSamplesWithinTime(List<EventSample> eventSamples, long fromTime, long untilTime) {
    int indexOfLower = Collections.binarySearch(eventSamples, fromTime, new EventSampleWithLongComparator());
    int indexOfUpper = Collections.binarySearch(eventSamples, untilTime, new EventSampleWithLongComparator());
    if (indexOfUpper >= 0 && indexOfLower >= 0) {
      return indexOfUpper - indexOfLower + 1;
    } else if (indexOfLower >= 0) {
      return -(indexOfUpper + 1) - indexOfLower;
    } else if (indexOfUpper >= 0) {
      return indexOfUpper + indexOfLower + 2;
    }
    return indexOfLower - indexOfUpper;
  }

  /**
   * @param eventSamples a sorted list of event samples (oldest to newest).
   */
  public static long oldestTimestamp(List<EventSample> eventSamples) {
    return eventSamples.get(0).getTimestampInMillis();
  }

  /**
   * @param eventSamples a sorted list of event samples (oldest to newest).
   */
  public static long newestTimestamp(List<EventSample> eventSamples) {
    return eventSamples.get(eventSamples.size() - 1).getTimestampInMillis();
  }

  public static int averageTimeOfDay(Iterable<EventSample> eventSamples) {
    Iterator<EventSample> eventSampleIterator = eventSamples.iterator();
    if (!eventSampleIterator.hasNext()) {
      throw new IllegalArgumentException("No event samples given. Could not calculate the average time of day of occurrences.");
    }
    int count = 0;
    long averageTimeOfDayInMillis = 0;
    do {
      ++count;
      DateTime timestamp = eventSampleIterator.next().getTimestamp();
      averageTimeOfDayInMillis += (long) timestamp.getMillisOfDay();
    } while (eventSampleIterator.hasNext());
    return (int) (averageTimeOfDayInMillis / count);
  }

  public static int averagePeriod(List<EventSample> eventSamples) {
    return averagePeriod(
      eventSamples.size(),
      eventSamples.get(0).getTimestampInMillis(),
      eventSamples.get(eventSamples.size() - 1).getTimestampInMillis()
    );
  }

  public static int averagePeriod(int numberOfOccurrences,
                                  long firstOccurrenceTimeInMillis,
                                  long lastOccurrenceTimeInMillis) {
    double durationInDays = (double) (lastOccurrenceTimeInMillis - firstOccurrenceTimeInMillis) / TimeUtils.DAY_IN_MILLIS;
    return (int) Math.round(durationInDays / (numberOfOccurrences - 1));
  }

  private static class EventSampleWithLongComparator implements Comparator<Object> {
    @Override
    public int compare(Object o1, Object o2) {
      EventSample eventSample = (EventSample)o1;
      Long bound = (Long) o2;
      return Long.compare(eventSample.getTimestampInMillis(), bound);
    }
  }
}
