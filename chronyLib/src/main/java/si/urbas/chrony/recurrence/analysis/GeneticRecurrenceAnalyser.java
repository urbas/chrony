package si.urbas.chrony.recurrence.analysis;

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
  private static final double CROSSOVER_RATIO = 0.1;
  private final List<Recurrence> foundRecurrences;

  public GeneticRecurrenceAnalyser(List<EventSample> eventSamples) {
    List<Recurrence> guessedRecurrences = guessPossibleRecurrences(eventSamples);
    GeneticAlgorithm geneticAlgorithm = createBinaryGeneticAlgorithm();
    ElitisticListPopulation initialPopulation = createInitialPopulation(eventSamples, guessedRecurrences);
    Population evolvedPopulation = geneticAlgorithm.evolve(initialPopulation, new FixedGenerationCount(MAX_GENERATIONS));
    RecurrenceChromosome fittestChromosome = (RecurrenceChromosome) evolvedPopulation.getFittestChromosome();
    foundRecurrences = new ArrayList<Recurrence>();
  }

  @Override
  public List<Recurrence> foundRecurrences() {
    return foundRecurrences;
  }

  private static List<Recurrence> guessPossibleRecurrences(List<EventSample> eventSamples) {
    return Arrays.<Recurrence>asList(new DailyPeriodRecurrence(1, 0, 0));
  }

  private static ElitisticListPopulation createInitialPopulation(List<EventSample> eventSamples, List<Recurrence> guessedRecurrences) {
    return new ElitisticListPopulation(createRandomPopulation(guessedRecurrences, eventSamples, new Random()), POPULATION_LIMIT, ELITISM_RATE);
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

  private static List<Chromosome> createRandomPopulation(List<Recurrence> guessedRecurrences, List<EventSample> eventSamples, Random randomnessSource) {
    ArrayList<Chromosome> population = new ArrayList<Chromosome>();
    RecurrenceFitnessPolicy recurrenceFitnessPolicy = new RecurrenceFitnessPolicy(eventSamples);
    for (int i = 0; i < POPULATION_LIMIT; i++) {
      List<Integer> randomListOfRecurrences = getRandomListOfRecurrences(guessedRecurrences, RATE_OF_GUESSED_RECURRENCE, randomnessSource);
      population.add(new RecurrenceChromosome(guessedRecurrences, randomListOfRecurrences, recurrenceFitnessPolicy));
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
