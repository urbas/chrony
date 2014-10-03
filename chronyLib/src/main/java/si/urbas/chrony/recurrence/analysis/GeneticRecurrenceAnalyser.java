package si.urbas.chrony.recurrence.analysis;

import org.apache.commons.math3.genetics.*;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.metrics.EventTemporalMetrics;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.recurrence.Recurrences;
import si.urbas.chrony.recurrence.RecurrencesList;
import si.urbas.chrony.util.TimeUtils;

import java.util.*;

public class GeneticRecurrenceAnalyser implements RecurrenceAnalyser {

  private static final int POPULATION_LIMIT = 20;
  private static final double CROSSOVER_RATE = 0.1;
  private static final double MUTATION_RATE = 0.05;
  private static final int TOURNAMENT_SELECTION_ARITY = 5;
  private static final double ELITISM_RATE = 0.15;
  private static final int MAX_GENERATIONS = 25;
  private static final double RATE_OF_GUESSED_RECURRENCE = 0.5;
  private static final double CROSSOVER_RATIO = 0.1;
  private final Recurrences foundRecurrences;

  public GeneticRecurrenceAnalyser(List<EventSample> eventSamples) {
    EventTemporalMetrics eventTemporalMetrics = EventTemporalMetrics.calculate(eventSamples);
    if (eventTemporalMetrics.entireTimeSpan() == 0) {
      foundRecurrences = RecurrencesList.emptyRecurrences;
    } else {
      RecurrenceFitnessPolicy fitnessPolicy = new RecurrenceFitnessPolicy(eventSamples, eventTemporalMetrics);
      List<Recurrence> guessedRecurrences = guessPossibleRecurrences(eventSamples);
      GeneticAlgorithm geneticAlgorithm = createBinaryGeneticAlgorithm();
      ElitisticListPopulation initialPopulation = createInitialPopulation(guessedRecurrences, fitnessPolicy);
      Population evolvedPopulation = geneticAlgorithm.evolve(initialPopulation, new FixedGenerationCount(MAX_GENERATIONS));
      foundRecurrences = (Recurrences) evolvedPopulation.getFittestChromosome();
    }
  }

  @Override
  public Recurrences foundRecurrences() {
    return foundRecurrences;
  }

  private static List<Recurrence> guessPossibleRecurrences(List<EventSample> eventSamples) {
    long timestampOfFirstEvent = eventSamples.get(0).getTimestamp();
    long timestampOfSecondEvent = eventSamples.get(1).getTimestamp();
    int periodInDays = (int) ((timestampOfSecondEvent - timestampOfFirstEvent) / TimeUtils.DAY_IN_MILLIS);
    Calendar eventTimestampCalendar = TimeUtils.toUtcCalendar(timestampOfFirstEvent);
    return Arrays.<Recurrence>asList(new DailyPeriodRecurrence(periodInDays, eventTimestampCalendar));
  }

  private static ElitisticListPopulation createInitialPopulation(List<Recurrence> guessedRecurrences, RecurrenceFitnessPolicy fitnessPolicy) {
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

  private static List<Chromosome> createRandomPopulation(List<Recurrence> guessedRecurrences, Random randomnessSource, RecurrenceFitnessPolicy fitnessPolicy) {
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
