package si.urbas.chrony.analysis;

import si.urbas.chrony.EventRepository;
import si.urbas.chrony.EventSample;

import java.util.Collection;

import static si.urbas.chrony.util.EventSampleUtils.*;

public class FrequencyAnalysis {
  private static final int ONE_DAY_IN_MILLIS = 24 * 3600 * 1000;
  private final EventRepository eventRepository;
  private final String eventName;

  public FrequencyAnalysis(EventRepository eventRepository, String eventName) {
    this.eventRepository = eventRepository;
    this.eventName = eventName;
  }

  /**
   * @return the number of occurrences divided by the given timespan (in occurrences per day).
   */
  public double frequency(long fromTime, long untilTime) {
    return frequency(eventRepository.samplesOf(eventName), fromTime, untilTime);
  }

  public double allTimeFrequency(long untilTime) {
    Collection<EventSample> eventSamples = eventRepository.samplesOf(eventName);
    if (eventSamples.isEmpty()) {
      return 0;
    } else {
      return frequency(eventSamples, getMinimumTimestamp(eventSamples), untilTime);
    }
  }

  private static double frequency(Collection<EventSample> eventSamples, long fromTime, long untilTime) {
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
