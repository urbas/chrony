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
  private final ArrayList<Recurrence> recurrences;
  private int activeRecurrencesCount;

  public RecurrenceChromosome(List<Recurrence> availableRecurrences, List<Integer> includedRecurrences, RecurrenceFitnessPolicy recurrenceFitnessPolicy) {
    super(includedRecurrences);
    assertRecurrenceListsSizesMatch(availableRecurrences, includedRecurrences);
    this.availableRecurrences = availableRecurrences;
    this.recurrenceFitnessPolicy = recurrenceFitnessPolicy;
    this.activeRecurrencesCount = countActiveRecurrences(includedRecurrences);
    this.recurrences = buildRecurrencesList(availableRecurrences, includedRecurrences);
  }

  @Override
  public AbstractListChromosome<Integer> newFixedLengthChromosome(List<Integer> chromosomeRepresentation) {
    return new RecurrenceChromosome(availableRecurrences, chromosomeRepresentation, recurrenceFitnessPolicy);
  }

  @Override
  public double fitness() {
    return recurrenceFitnessPolicy.fitness(this);
  }

  @Override
  public int getRecurrencesCount() {
    return activeRecurrencesCount;
  }

  @Override
  public List<Recurrence> getRecurrences() {
    return recurrences;

  }

  private static ArrayList<Recurrence> buildRecurrencesList(List<Recurrence> availableRecurrences, List<Integer> includedRecurrences) {
    ArrayList<Recurrence> recurrences = new ArrayList<Recurrence>();
    for (int i = 0; i < includedRecurrences.size(); i++) {
      Integer isRecurrenceIncluded = includedRecurrences.get(i);
      if (isRecurrenceIncluded == 1) {
        recurrences.add(availableRecurrences.get(i));
      }
    }
    return recurrences;
  }

  private static int countActiveRecurrences(List<Integer> includedRecurrences) {
    int count = 0;
    for (Integer isRecurrenceIncluded : includedRecurrences) {
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
