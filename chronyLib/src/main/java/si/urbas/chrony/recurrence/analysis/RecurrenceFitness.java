package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.IndexedRecurrence;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.List;

import static java.lang.Math.abs;
import static si.urbas.chrony.util.EventSampleUtils.distanceBetween;
import static si.urbas.chrony.util.EventSampleUtils.findClosest;

public class RecurrenceFitness {


  public double fitness(Recurrence recurrence, List<EventSample> eventSamples) {
    if (eventSamples.size() > 0) {
      return -sumOfDistancesFromSamples(recurrence, eventSamples)
             - sumOfDistancesToSamples(recurrence, eventSamples);
    } else {
      return Double.NEGATIVE_INFINITY;
    }
  }

  public static double[] computeFitnesses(List<Recurrence> recurrences, List<EventSample> eventSamples) {
    double[] recurrenceFitnesses = new double[recurrences.size()];
    RecurrenceFitness recurrenceFitness = new RecurrenceFitness();
    int idx = -1;
    for (Recurrence recurrence : recurrences) {
      recurrenceFitnesses[++idx] = recurrenceFitness.fitness(recurrence, eventSamples);
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
    IndexedRecurrence occurrencesInEntireRange = recurrence.subOccurrences(timestampOfFirstSample, timestampOfLastSample);
    for (int i = 0; i < occurrencesInEntireRange.size(); i++) {
      distanceSum += distanceToClosestSample(occurrencesInEntireRange.getOccurrenceAt(i), eventSamples);
    }
    return distanceSum;
  }

  private double distanceToClosestSample(long occurrenceInMillis, List<EventSample> eventSamples) {
    return distanceBetween(findClosest(eventSamples, occurrenceInMillis), occurrenceInMillis);
  }

}
