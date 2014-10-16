package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.ArrayList;
import java.util.List;

public class BitMaskChromosomeFitness {

  private static final int SIZE_PENALTY_RATE = 1000 * 60 * 60;
  private final List<EventSample> eventSamples;
  private final ArrayList<RecurrenceFitness> recurrenceFitnesses;

  public BitMaskChromosomeFitness(List<EventSample> eventSamples, List<Recurrence> recurrencePool) {
    this.eventSamples = eventSamples;
    this.recurrenceFitnesses = RecurrenceFitness.computeFitnesses(recurrencePool, eventSamples);
  }

  public double fitness(RecurrenceChromosome recurrences) {
    if (eventSamples.size() < 2 && recurrences.getRecurrencesCount() > 0) {
      return Double.NEGATIVE_INFINITY;
    } else {
      int sizePenalty = sizePenalty(recurrences.getRecurrences());
      return -sizePenalty + sumOfRecurrencesFitnesses(recurrences);
    }
  }

  private double sumOfRecurrencesFitnesses(RecurrenceChromosome recurrences) {
    double sumOfRecurrenceFitnesses = 0;
    for (int i = 0; i < recurrenceFitnesses.size(); i++) {
      if (recurrences.hasRecurrence(i)) {
        sumOfRecurrenceFitnesses += recurrenceFitnesses.get(i).fitness();
      }
    }
    return sumOfRecurrenceFitnesses;
  }

  private static int sizePenalty(List<Recurrence> recurrences) {
    return recurrences.size() * SIZE_PENALTY_RATE;
  }

}
