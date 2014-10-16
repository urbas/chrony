package si.urbas.chrony.recurrence.analysis;

import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.BinaryChromosome;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.ArrayList;
import java.util.List;

public class RecurrenceChromosome extends BinaryChromosome {

  private final List<Recurrence> availableRecurrences;
  private final BitMaskChromosomeFitness bitMaskChromosomeFitness;
  private final ArrayList<Recurrence> recurrences;
  private double cachedFitness;
  private boolean isFitnessInitialised = false;

  public RecurrenceChromosome(List<Recurrence> availableRecurrences,
                              List<Integer> includedRecurrences,
                              BitMaskChromosomeFitness bitMaskChromosomeFitness) {
    super(includedRecurrences);
    assertRecurrenceListsSizesMatch(availableRecurrences, includedRecurrences);
    this.availableRecurrences = availableRecurrences;
    this.bitMaskChromosomeFitness = bitMaskChromosomeFitness;
    this.recurrences = buildRecurrencesList(availableRecurrences, includedRecurrences);
  }

  @Override
  public AbstractListChromosome<Integer> newFixedLengthChromosome(List<Integer> includedRecurrences) {
    return new RecurrenceChromosome(availableRecurrences, includedRecurrences, bitMaskChromosomeFitness);
  }

  @Override
  public double fitness() {
    if (!isFitnessInitialised) {
      cachedFitness = bitMaskChromosomeFitness.fitness(this);
      isFitnessInitialised = true;
    }
    return cachedFitness;
  }

  public int getRecurrencesCount() {
    return recurrences.size();
  }

  public List<Recurrence> getRecurrences() {
    return recurrences;
  }

  public boolean hasRecurrence(int indexOfRecurrence) {
    return getRepresentation().get(indexOfRecurrence) == 1;
  }

  private static ArrayList<Recurrence> buildRecurrencesList(List<Recurrence> availableRecurrences,
                                                            List<Integer> includedRecurrences) {
    ArrayList<Recurrence> recurrences = new ArrayList<Recurrence>();
    for (int i = 0; i < includedRecurrences.size(); i++) {
      Integer isRecurrenceIncluded = includedRecurrences.get(i);
      if (isRecurrenceIncluded == 1) {
        recurrences.add(availableRecurrences.get(i));
      }
    }
    return recurrences;
  }

  private static void assertRecurrenceListsSizesMatch(List<Recurrence> availableRecurrences,
                                                      List<Integer> includedRecurrences) {
    if (availableRecurrences.size() != includedRecurrences.size()) {
      throw new IllegalArgumentException("The list of available recurrences must match the size of included recurrences.");
    }
  }
}
