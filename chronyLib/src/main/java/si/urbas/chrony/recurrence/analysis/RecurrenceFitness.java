package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.Occurrences;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static si.urbas.chrony.util.EventSampleAssertions.assertEventSamplesOrdered;
import static si.urbas.chrony.util.EventSampleUtils.distanceBetween;
import static si.urbas.chrony.util.EventSampleUtils.findClosest;

public class RecurrenceFitness {

  private final double fitness;

  /**
   * @param eventSamples event samples ordered by ascending timestamp.
   */
  public RecurrenceFitness(Recurrence recurrence, List<EventSample> eventSamples) {
    assertEventSamplesOrdered(eventSamples);
    if (eventSamples.size() > 0) {
      fitness = -sumOfDistancesFromSamples(recurrence, eventSamples)
                - sumOfDistancesToSamples(recurrence, eventSamples);
    } else {
      fitness = Double.NEGATIVE_INFINITY;
    }
  }

  public double fitness() {
    return fitness;
  }

  public static ArrayList<RecurrenceFitness> computeFitnesses(List<Recurrence> recurrences, List<EventSample> eventSamples) {
    ArrayList<RecurrenceFitness> recurrenceFitnesses = new ArrayList<RecurrenceFitness>();
    for (Recurrence recurrence : recurrences) {
      recurrenceFitnesses.add(new RecurrenceFitness(recurrence, eventSamples));
    }
    return recurrenceFitnesses;
  }

  private static double sumOfDistancesFromSamples(Recurrence recurrence, List<EventSample> eventSamples) {
    double sum = 0;
    for (EventSample eventSample : eventSamples) {
      sum += abs(recurrence.distanceTo(eventSample.getTimestampInMillis()));
    }
    return sum;
  }

  private double sumOfDistancesToSamples(Recurrence recurrence, List<EventSample> eventSamples) {
    double distanceSum = 0;
    long timestampOfFirstSample = eventSamples.get(0).getTimestampInMillis();
    long timestampOfLastSample = eventSamples.get(eventSamples.size() - 1).getTimestampInMillis();
    Occurrences occurrencesInEntireRange = recurrence.getSubOccurrences(timestampOfFirstSample, timestampOfLastSample);
    for (int i = 0; i < occurrencesInEntireRange.size(); i++) {
      distanceSum += distanceToClosestSample(occurrencesInEntireRange.getOccurrenceAt(i), eventSamples);
    }
    return distanceSum;
  }

  private double distanceToClosestSample(long occurrenceInMillis, List<EventSample> eventSamples) {
    return distanceBetween(findClosest(eventSamples, occurrenceInMillis), occurrenceInMillis);
  }


}
