package si.urbas.chrony.recurrence.analysis;

import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.BinaryChromosome;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.recurrence.Recurrences;

import java.util.ArrayList;
import java.util.List;

public class RecurrenceChromosome extends BinaryChromosome implements Recurrences {

  private final List<Recurrence> availableRecurrences;
  private final RecurrenceFitnessPolicy recurrenceFitnessPolicy;
  private int activeRecurrencesCount;

  public RecurrenceChromosome(List<Recurrence> availableRecurrences, List<Integer> includedRecurrences, RecurrenceFitnessPolicy recurrenceFitnessPolicy) {
    super(includedRecurrences);
    assertRecurrenceListsSizesMatch(availableRecurrences, includedRecurrences);
    this.availableRecurrences = availableRecurrences;
    this.recurrenceFitnessPolicy = recurrenceFitnessPolicy;
    this.activeRecurrencesCount = countActiveRecurrences();
  }

  @Override
  public AbstractListChromosome<Integer> newFixedLengthChromosome(List<Integer> chromosomeRepresentation) {
    return new RecurrenceChromosome(availableRecurrences, chromosomeRepresentation, recurrenceFitnessPolicy);
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
  public int getRecurrencesCount() {
    return activeRecurrencesCount;
  }

  private int countActiveRecurrences() {
    int count = 0;
    for (Integer isRecurrenceIncluded : getRepresentation()) {
      if (isRecurrenceIncluded != 0) {
        ++count;
      }
    }
    return count;
  }

  private static void assertRecurrenceListsSizesMatch(List<Recurrence> availableRecurrences, List<Integer> includedRecurrences) {
    if (availableRecurrences.size() != includedRecurrences.size()) {
      throw new IllegalArgumentException("The list of available recurrences must match the size of included recurrences.");
    }
  }
}
