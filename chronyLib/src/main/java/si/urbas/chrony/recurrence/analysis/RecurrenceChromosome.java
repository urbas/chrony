package si.urbas.chrony.recurrence.analysis;

import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.BinaryChromosome;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.recurrence.Recurrences;

import java.util.ArrayList;
import java.util.List;

public class RecurrenceChromosome extends BinaryChromosome implements Recurrences {

  private final List<EventSample> eventSamples;
  private final List<Recurrence> availableRecurrences;
  private final RecurrenceFitnessPolicy recurrenceFitnessPolicy;

  public RecurrenceChromosome(List<Recurrence> availableRecurrences, List<EventSample> eventSamples, List<Integer> includedRecurrences, RecurrenceFitnessPolicy recurrenceFitnessPolicy) {
    super(includedRecurrences);
    this.availableRecurrences = availableRecurrences;
    this.eventSamples = eventSamples;
    this.recurrenceFitnessPolicy = recurrenceFitnessPolicy;
  }

  @Override
  public AbstractListChromosome<Integer> newFixedLengthChromosome(List<Integer> chromosomeRepresentation) {
    return new RecurrenceChromosome(availableRecurrences, eventSamples, chromosomeRepresentation, recurrenceFitnessPolicy);
  }

  @Override
  public double fitness() {
    return recurrenceFitnessPolicy.fitness(this);
  }

  public List<Recurrence> getRecurrences(List<Recurrence> guessedRecurrences) {
    ArrayList<Recurrence> recurrences = new ArrayList<Recurrence>();
    List<Integer> representation = getRepresentation();
    for (int i = 0; i < representation.size(); i++) {
      Integer isRecurrenceIncluded = representation.get(i);
      if (isRecurrenceIncluded == 1) {
        recurrences.add(guessedRecurrences.get(i));
      }
    }
    return recurrences;
  }

  @Override
  public int size() {
    return getRepresentation().size();
  }
}
