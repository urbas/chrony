package si.urbas.chrony.frequency.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.util.EventSampleUtils;

import java.util.List;

import static si.urbas.chrony.util.EventSampleUtils.countSamplesWithinTime;
import static si.urbas.chrony.util.EventSampleUtils.oldestTimestamp;

public class FrequencyAnalysis {

  private static final int ONE_DAY_IN_MILLIS = 24 * 3600 * 1000;
  private final List<EventSample> eventSamples;

  public FrequencyAnalysis(List<EventSample> eventSamples) {
    this.eventSamples = eventSamples;
  }

  /**
   * @return the number of occurrences divided by the given timespan (in occurrences per day).
   */
  public double frequency(long fromTime, long untilTime) {
    return frequency(eventSamples, fromTime, untilTime);
  }

  public double frequencyUntil(long untilTime) {
    if (eventSamples.isEmpty()) {
      return 0;
    } else {
      return frequency(eventSamples, oldestTimestamp(eventSamples).getMillis(), untilTime);
    }
  }

  public int occurrencesUntil(long untilTime) {
    return occurrencesWithin(0, untilTime);
  }

  public int occurrencesWithin(long fromTime, long untilTime) {
    return EventSampleUtils.countSamplesWithinTime(eventSamples, fromTime, untilTime);
  }

  private static double frequency(List<EventSample> eventSamples, long fromTime, long untilTime) {
    if (eventSamples.isEmpty()) {
      return 0;
    } else {
      int numberOfOccurrencesInPeriod = countSamplesWithinTime(eventSamples, fromTime, untilTime);
      if (fromTime == untilTime) {
        return numberOfOccurrencesInPeriod;
      } else {
        return (double) numberOfOccurrencesInPeriod / (untilTime - fromTime) * ONE_DAY_IN_MILLIS;
      }
    }
  }
}
