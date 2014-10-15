package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.recurrence.Recurrences;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class RecurrenceFitness {

  private final double fitness;

  public RecurrenceFitness(Recurrence recurrence, List<EventSample> eventSamples) {
    if (eventSamples.size() > 0) {
      fitness = -sumOfDistances(recurrence, eventSamples);
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

}
