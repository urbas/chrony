package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.metrics.EventTemporalMetrics;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.recurrence.Recurrences;

import java.util.List;

import static java.lang.Math.abs;

public class RecurrenceFitnessPolicy {

  private static final int SIZE_PENALTY_RATE = -1000;
  private final List<EventSample> eventSamples;

  public RecurrenceFitnessPolicy(List<EventSample> eventSamples) {
    this.eventSamples = eventSamples;
  }

  public double fitness(Recurrences recurrences) {
    return sizePenalty(recurrences) -
           minimumDistancesSum(recurrences) -
           distancesOfOccurrencesToSamplesSum(recurrences);
  }

  private static int sizePenalty(Recurrences recurrences) {
    return recurrences.getRecurrencesCount() * SIZE_PENALTY_RATE;
  }

  private int minimumDistancesSum(Recurrences recurrences) {
    int penalty = 0;
    for (EventSample eventSample : eventSamples) {
      penalty += findMinimumDistance(recurrences, eventSample);
    }
    return penalty;
  }

  private double distancesOfOccurrencesToSamplesSum(Recurrences recurrences) {
    EventTemporalMetrics temporalMetrics = EventTemporalMetrics.calculate(eventSamples);
    int distanceSum = 0;
    for (Recurrence recurrence : recurrences.getRecurrences()) {
      List<Long> occurrencesInEntireRange = recurrence.getOccurrencesBetween(temporalMetrics.oldestTimestamp, temporalMetrics.newestTimestamp);
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
    long minimumDistance = 0;
    for (Recurrence recurrence : recurrences.getRecurrences()) {
      long currentRecurrenceDistance = abs(recurrence.distanceTo(eventSample.getTimestamp()));
      if (currentRecurrenceDistance >= minimumDistance) {
        minimumDistance = currentRecurrenceDistance;
      }
    }
    return minimumDistance;
  }
}
