package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.recurrence.Recurrences;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static si.urbas.chrony.util.EventSampleAssertions.assertEventSamplesOrdered;

public class RecurrenceFitness {

  private final double fitness;

  public RecurrenceFitness(Recurrence recurrence, List<EventSample> eventSamples) {
    assertEventSamplesOrdered(eventSamples);
    if (eventSamples.size() > 0) {
      fitness = -sumOfDistances(recurrence, eventSamples) - spuriousOccurrencesPenalty(recurrence, eventSamples);
    } else {
      fitness = 0;
    }
  }

  public double fitness() {
    return fitness;
  }

  public static ArrayList<RecurrenceFitness> computeFitnesses(Recurrences recurrences, List<EventSample> eventSamples) {
    return computeFitnesses(recurrences.getRecurrences(), eventSamples);
  }

  public static ArrayList<RecurrenceFitness> computeFitnesses(List<Recurrence> recurrences, List<EventSample> eventSamples) {
    return new ArrayList<RecurrenceFitness>();
  }

  private static double sumOfDistances(Recurrence recurrence, List<EventSample> eventSamples) {
    double sum = 0;
    for (EventSample eventSample : eventSamples) {
      sum += abs(recurrence.distanceTo(eventSample.getTimestamp()));
    }
    return sum;
  }

  private double spuriousOccurrencesPenalty(Recurrence recurrence, List<EventSample> eventSamples) {
    double distanceSum = 0;
    long timestampOfFirstSample = eventSamples.get(0).getTimestamp();
    long timestampOfLastSample = eventSamples.get(eventSamples.size() - 1).getTimestamp();
    List<Long> occurrencesInEntireRange = recurrence.getOccurrencesBetween(timestampOfFirstSample, timestampOfLastSample);
    for (Long occurrence : occurrencesInEntireRange) {
      distanceSum += distanceToClosestSample(occurrence, eventSamples);
    }
    return distanceSum;
  }

  private double distanceToClosestSample(long occurrenceInMillis, List<EventSample> eventSamples) {
    long minDistance = Long.MAX_VALUE;
    // TODO: Implement this via binary search.
    for (EventSample eventSample : eventSamples) {
      long currentDistance = abs(eventSample.getTimestamp() - occurrenceInMillis);
      if (currentDistance < minDistance) {
        minDistance = currentDistance;
      }
    }
    return minDistance;
  }


}
