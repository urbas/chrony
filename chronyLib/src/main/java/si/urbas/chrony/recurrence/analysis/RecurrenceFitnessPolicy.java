package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.metrics.EventTemporalMetrics;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.recurrence.Recurrences;

import java.util.List;

import static java.lang.Math.abs;

public class RecurrenceFitnessPolicy {

  private static final int SIZE_PENALTY_RATE = 1000 * 60 * 60;
  private final List<EventSample> eventSamples;
  private EventTemporalMetrics eventSamplesTemporalMetrics;

  /**
   * @param eventSamples a list o event samples that has to be ordered by the sample's timestamp in an increasing order.
   */
  public RecurrenceFitnessPolicy(List<EventSample> eventSamples) {
    this(eventSamples, EventTemporalMetrics.calculate(eventSamples));
  }

  public RecurrenceFitnessPolicy(List<EventSample> eventSamples, EventTemporalMetrics eventTemporalMetrics) {
    this.eventSamples = eventSamples;
    this.eventSamplesTemporalMetrics = eventTemporalMetrics;
  }

  public double fitness(Recurrences recurrences) {
    if (eventSamples.size() < 2 && recurrences.getRecurrencesCount() > 0) {
      return Double.NEGATIVE_INFINITY;
    } else {
      int sizePenalty = sizePenalty(recurrences);
      long minimumDistancesPenalty = minimumDistancesPenalty(recurrences, eventSamplesTemporalMetrics);
      long spuriousOccurrencesPenalty = spuriousOccurrencesPenalty(recurrences, eventSamplesTemporalMetrics);
      return -sizePenalty - minimumDistancesPenalty - spuriousOccurrencesPenalty;
    }
  }

  private static int sizePenalty(Recurrences recurrences) {
    return recurrences.getRecurrencesCount() * SIZE_PENALTY_RATE;
  }

  private long minimumDistancesPenalty(Recurrences recurrences, EventTemporalMetrics eventSamplesTemporalMetrics) {
    if (recurrences.getRecurrencesCount() == 0) {
      return defaultMinimumDistancePenalty(eventSamplesTemporalMetrics);
    } else {
      return sumOfMinDistances(recurrences);
    }
  }

  private long sumOfMinDistances(Recurrences recurrences) {
    int penalty = 0;
    for (EventSample eventSample : eventSamples) {
      penalty += findMinimumDistance(recurrences, eventSample);
    }
    return penalty;
  }

  private long defaultMinimumDistancePenalty(EventTemporalMetrics eventSamplesTemporalMetrics) {
    return eventSamplesTemporalMetrics.entireTimeSpan() * eventSamples.size() / 2;
  }

  private long spuriousOccurrencesPenalty(Recurrences recurrences, EventTemporalMetrics eventSamplesTemporalMetrics) {
    int distanceSum = 0;
    for (Recurrence recurrence : recurrences.getRecurrences()) {
      List<Long> occurrencesInEntireRange = recurrence.getOccurrencesBetween(eventSamplesTemporalMetrics.oldestTimestamp, eventSamplesTemporalMetrics.newestTimestamp);
      for (Long occurrence : occurrencesInEntireRange) {
        distanceSum += distanceToClosestSample(occurrence);
      }
    }
    return distanceSum;
  }

  private long distanceToClosestSample(long occurrenceTimeInMillis) {
    long minDistance = Long.MAX_VALUE;
    for (EventSample eventSample : eventSamples) {
      long currentDistance = abs(eventSample.getTimestamp() - occurrenceTimeInMillis);
      if (currentDistance < minDistance) {
        minDistance = currentDistance;
      }
    }
    return minDistance;
  }

  /**
   * @return difference in milliseconds between the closest recurrence and the timestamp of this event sample.
   */
  private long findMinimumDistance(Recurrences recurrences, EventSample eventSample) {
    long minimumDistance = Long.MAX_VALUE;
    for (Recurrence recurrence : recurrences.getRecurrences()) {
      long currentRecurrenceDistance = abs(recurrence.distanceTo(eventSample.getTimestamp()));
      if (currentRecurrenceDistance <= minimumDistance) {
        minimumDistance = currentRecurrenceDistance;
      }
    }
    return minimumDistance;
  }
}
