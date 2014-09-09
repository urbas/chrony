package si.urbas.chrony.util;

import si.urbas.chrony.EventSample;

import java.util.Collection;
import java.util.Collections;

public class EventSampleUtils {

  public static int countSamplesWithinTime(Collection<EventSample> eventSamples, long fromTime, long untilTime) {
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
}
