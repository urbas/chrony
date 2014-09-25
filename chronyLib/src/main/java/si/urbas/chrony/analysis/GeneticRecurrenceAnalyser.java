package si.urbas.chrony.analysis;

import org.apache.commons.math3.genetics.*;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GeneticRecurrenceAnalyser implements RecurrenceAnalyser {

  private static final int POPULATION_LIMIT = 20;
  private static final double CROSSOVER_RATE = 0.1;
  private static final double MUTATION_RATE = 0.05;
  private static final int TOURNAMENT_SELECTION_ARITY = 5;
  private static final double ELITISM_RATE = 0.15;
  private static final int MAX_GENERATIONS = 25;
  private static final double RATE_OF_GUESSED_RECURRENCE = 0.1;
  private final List<Recurrence> foundRecurrences;

  public GeneticRecurrenceAnalyser(List<EventSample> eventSamples) {
    List<? extends Recurrence> guessedRecurrences = guessPossibleRecurrences(eventSamples);
    GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(
      new OnePointCrossover<Integer>(),
      CROSSOVER_RATE,
      new BinaryMutation(),
      MUTATION_RATE,
      new TournamentSelection(TOURNAMENT_SELECTION_ARITY)
    );
    Population evolvedPopulation = geneticAlgorithm.evolve(
      new ElitisticListPopulation(getInitialPopulation(guessedRecurrences, eventSamples, new Random()), POPULATION_LIMIT, ELITISM_RATE),
      new FixedGenerationCount(MAX_GENERATIONS)
    );
    RecurrenceChromosome fittestChromosome = (RecurrenceChromosome) evolvedPopulation.getFittestChromosome();
    foundRecurrences = new ArrayList<Recurrence>();
  }

  @Override
  public List<Recurrence> foundRecurrences() {
    return foundRecurrences;
  }

  private List<? extends Recurrence> guessPossibleRecurrences(List<EventSample> eventSamples) {
    return Arrays.asList(new DailyPeriodRecurrence(1, 0, 0));
  }

  private static List<Chromosome> getInitialPopulation(List<? extends Recurrence> guessedRecurrences, List<EventSample> eventSamples, Random randomnessSource) {
    ArrayList<Chromosome> population = new ArrayList<Chromosome>();
    for (int i = 0; i < POPULATION_LIMIT; i++) {
      List<Integer> randomListOfRecurrences = getRandomListOfRecurrences(guessedRecurrences, RATE_OF_GUESSED_RECURRENCE, randomnessSource);
      population.add(new RecurrenceChromosome(randomListOfRecurrences, eventSamples));
    }
    return population;
  }

  private static List<Integer> getRandomListOfRecurrences(List<? extends Recurrence> guessedRecurrences, double rateOfInclusion, Random randomnessSource) {
    ArrayList<Integer> includedRecurrences = new ArrayList<Integer>(guessedRecurrences.size());
    for (Recurrence ignored : guessedRecurrences) {
      int shouldIncludeRecurrence = randomnessSource.nextFloat() < rateOfInclusion ? 1 : 0;
      includedRecurrences.add(shouldIncludeRecurrence);
    }
    return includedRecurrences;
  }

  private static class RecurrenceChromosome extends BinaryChromosome {

    private final List<EventSample> eventSamples;

    public RecurrenceChromosome(List<Integer> includedRecurrences, List<EventSample> eventSamples) {
      super(includedRecurrences);
      this.eventSamples = eventSamples;
    }

    @Override
    public AbstractListChromosome<Integer> newFixedLengthChromosome(List<Integer> chromosomeRepresentation) {
      return new RecurrenceChromosome(chromosomeRepresentation, eventSamples);
    }

    @Override
    public double fitness() {
      double fitness = 0;

      return fitness;
    }

    public List<Recurrence> getRecurrences(List<? extends Recurrence> guessedRecurrences) {
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
  }
}
