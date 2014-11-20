package si.urbas.chrony.util;

import org.joda.time.DateTime;
import si.urbas.chrony.EventSample;

import java.util.*;

import static java.lang.Math.abs;

public class EventSampleUtils {

  /**
   * @param eventSamples a sorted list of event samples (oldest to newest).
   */
  public static EventSample findClosest(List<EventSample> eventSamples, long millis) {
    int insertionIndex = Collections.binarySearch(eventSamples, millis, EventSampleWithTimestampLongComparator.INSTANCE);
    if (insertionIndex == -1) {
      return eventSamples.get(0);
    } else if (insertionIndex < -eventSamples.size()) {
      return eventSamples.get(eventSamples.size() - 1);
    } else if (insertionIndex < 0) {
      insertionIndex = -(insertionIndex + 1);
      EventSample olderEventSample = eventSamples.get(insertionIndex - 1);
      EventSample newerEventSample = eventSamples.get(insertionIndex);
      if (distanceBetween(olderEventSample, millis) < distanceBetween(newerEventSample, millis)) {
        return olderEventSample;
      } else {
        return newerEventSample;
      }
    } else {
      return eventSamples.get(insertionIndex);
    }
  }

  public static long distanceBetween(EventSample olderEventSample, long millis) {
    return abs(olderEventSample.getTimestamp().getMillis() - millis);
  }

  /**
   * Sorts the given list by the increasing timestamps of the event samples (oldest to newest).
   *
   * @return the same list (same instance ) as the given {@code eventSamples}.
   */
  public static List<EventSample> sortByTimestamp(List<EventSample> eventSamples) {
    Collections.sort(eventSamples, EventSampleOldestFirstComparator.INSTANCE);
    return eventSamples;
  }

  /**
   * @param eventSamples a sorted list of event samples (oldest to newest).
   * @return the number of event samples with timestamps between {@code fromTime} (inclusive) and {@code untilTime}
   * (inclusive).
   */
  public static int countSamplesWithinTime(List<EventSample> eventSamples, long fromTime, long untilTime) {
    int indexOfLower = Collections.binarySearch(eventSamples, fromTime, EventSampleWithTimestampLongComparator.INSTANCE);
    int indexOfUpper = Collections.binarySearch(eventSamples, untilTime, EventSampleWithTimestampLongComparator.INSTANCE);
    if (indexOfLower < 0) {
      if (indexOfUpper < 0) {
        return indexOfLower - indexOfUpper;
      } else {
        return indexOfUpper + indexOfLower + 2;
      }
    } else if (indexOfUpper < 0) {
      return -(indexOfUpper + 1) - indexOfLower;
    } else {
      return indexOfUpper - indexOfLower + 1;
    }
  }

  /**
   * @param eventSamples a sorted list of event samples (oldest to newest).
   */
  public static DateTime oldestTimestamp(List<EventSample> eventSamples) {
    return eventSamples.get(0).getTimestamp();
  }

  /**
   * @param eventSamples a sorted list of event samples (oldest to newest).
   */
  public static DateTime newestTimestamp(List<EventSample> eventSamples) {
    return eventSamples.get(eventSamples.size() - 1).getTimestamp();
  }

  /**
   * @param eventSamples a sorted list of event samples (oldest to newest).
   */
  public static int averageTimeOfDay(Iterable<EventSample> eventSamples) {
    Iterator<EventSample> eventSampleIterator = eventSamples.iterator();
    if (!eventSampleIterator.hasNext()) {
      throw new IllegalArgumentException("No event samples given. Could not calculate the average time of day of occurrences.");
    }
    int count = 0;
    long summedTimeOfDayInMillis = 0;
    do {
      ++count;
      DateTime timestamp = eventSampleIterator.next().getTimestamp();
      summedTimeOfDayInMillis += (long) timestamp.getMillisOfDay();
    } while (eventSampleIterator.hasNext());
    return (int) (summedTimeOfDayInMillis / count);
  }

  /**
   * @param eventSamples a sorted list of event samples (oldest to newest).
   */
  public static int averagePeriodInDays(List<EventSample> eventSamples) {
    return averagePeriodInDays(
      eventSamples.size(),
      eventSamples.get(0).getTimestamp().getMillis(),
      eventSamples.get(eventSamples.size() - 1).getTimestamp().getMillis()
    );
  }

  public static int averagePeriodInDays(int numberOfOccurrences,
                                        long firstOccurrenceTimeInMillis,
                                        long lastOccurrenceTimeInMillis) {
    double durationInDays = (double) (lastOccurrenceTimeInMillis - firstOccurrenceTimeInMillis) / TimeUtils.DAY_IN_MILLIS;
    return (int) Math.round(durationInDays / (numberOfOccurrences - 1));
  }

  private static class EventSampleWithTimestampLongComparator implements Comparator<Object> {
    private static final EventSampleWithTimestampLongComparator INSTANCE = new EventSampleWithTimestampLongComparator();

    @Override
    public int compare(Object o1, Object o2) {
      EventSample eventSample = (EventSample) o1;
      Long bound = (Long) o2;
      return Long.compare(eventSample.getTimestamp().getMillis(), bound);
    }
  }
}
