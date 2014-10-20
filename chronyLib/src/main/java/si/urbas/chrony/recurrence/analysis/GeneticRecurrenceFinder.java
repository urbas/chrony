package si.urbas.chrony.recurrence.analysis;

import org.apache.commons.math3.genetics.*;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.metrics.EventTemporalMetrics;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.*;

import static si.urbas.chrony.util.EventSampleAssertions.assertEventSamplesOrdered;

public class GeneticRecurrenceFinder implements RecurrenceFinder {

  private static final int POPULATION_LIMIT = 20;
  private static final double CROSSOVER_RATE = 0.1;
  private static final double MUTATION_RATE = 0.05;
  private static final int TOURNAMENT_SELECTION_ARITY = 5;
  private static final double ELITISM_RATE = 0.15;
  private static final int MAX_GENERATIONS = 2500;
  private static final double RATE_OF_GUESSED_RECURRENCE = 0.5;
  private static final double CROSSOVER_RATIO = 0.1;
  private final List<Recurrence> foundRecurrences;

  public GeneticRecurrenceFinder(List<EventSample> eventSamples, RecurrenceFinder foundRecurrencesToOptimize) {
    assertEventSamplesOrdered(eventSamples);
    EventTemporalMetrics eventTemporalMetrics = EventTemporalMetrics.calculate(eventSamples);
    if (eventTemporalMetrics.entireTimeSpan() == 0) {
      foundRecurrences = Collections.emptyList();
    } else {
      List<Recurrence> guessedRecurrences = foundRecurrencesToOptimize.foundRecurrences();
      BitMaskChromosomeFitness fitnessPolicy = new BitMaskChromosomeFitness(eventSamples, guessedRecurrences);
      GeneticAlgorithm geneticAlgorithm = createBinaryGeneticAlgorithm();
      ElitisticListPopulation initialPopulation = createInitialPopulation(guessedRecurrences, fitnessPolicy);
      Population evolvedPopulation = geneticAlgorithm.evolve(initialPopulation, new FixedGenerationCount(MAX_GENERATIONS));
      foundRecurrences = ((RecurrenceChromosome) evolvedPopulation.getFittestChromosome()).getRecurrences();
    }
  }

  @Override
  public List<Recurrence> foundRecurrences() {
    return foundRecurrences;
  }

  private static ElitisticListPopulation createInitialPopulation(List<Recurrence> guessedRecurrences, BitMaskChromosomeFitness fitnessPolicy) {
    return new ElitisticListPopulation(createRandomPopulation(guessedRecurrences, new Random(), fitnessPolicy), POPULATION_LIMIT, ELITISM_RATE);
  }

  private static GeneticAlgorithm createBinaryGeneticAlgorithm() {
    return new GeneticAlgorithm(
      new UniformCrossover<Integer>(CROSSOVER_RATIO),
      CROSSOVER_RATE,
      new BinaryMutation(),
      MUTATION_RATE,
      new TournamentSelection(TOURNAMENT_SELECTION_ARITY)
    );
  }

  private static List<Chromosome> createRandomPopulation(List<Recurrence> guessedRecurrences, Random randomnessSource, BitMaskChromosomeFitness fitnessPolicy) {
    ArrayList<Chromosome> population = new ArrayList<Chromosome>();
    for (int i = 0; i < POPULATION_LIMIT; i++) {
      List<Integer> randomListOfRecurrences = getRandomListOfRecurrences(guessedRecurrences, RATE_OF_GUESSED_RECURRENCE, randomnessSource);
      population.add(new RecurrenceChromosome(guessedRecurrences, randomListOfRecurrences, fitnessPolicy));
    }
    return population;
  }

  private static List<Integer> getRandomListOfRecurrences(List<Recurrence> guessedRecurrences, double rateOfInclusion, Random randomnessSource) {
    ArrayList<Integer> includedRecurrences = new ArrayList<Integer>(guessedRecurrences.size());
    for (Recurrence ignored : guessedRecurrences) {
      int shouldIncludeRecurrence = randomnessSource.nextFloat() < rateOfInclusion ? 1 : 0;
      includedRecurrences.add(shouldIncludeRecurrence);
    }
    return includedRecurrences;
  }

}
