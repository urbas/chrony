package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.Recurrences;

import java.util.List;

public class RecurrenceFitnessPolicy {

  private static final int SIZE_PENALTY = -1000;

  public RecurrenceFitnessPolicy(List<EventSample> eventSamples) {

  }

  public double fitness(Recurrences recurrences) {
    return recurrences.size() * SIZE_PENALTY;
  }
}
