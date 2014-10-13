package si.urbas.chrony.recurrence.analysis;

import org.apache.commons.math3.genetics.*;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.metrics.EventTemporalMetrics;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.recurrence.Recurrences;
import si.urbas.chrony.recurrence.RecurrencesList;
import si.urbas.chrony.util.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static si.urbas.chrony.util.EventSampleAssertions.assertEventSamplesOrdered;

public class GeneticRecurrenceAnalyser implements RecurrenceAnalyser {

  private static final int POPULATION_LIMIT = 20;
  private static final double CROSSOVER_RATE = 0.1;
  private static final double MUTATION_RATE = 0.05;
  private static final int TOURNAMENT_SELECTION_ARITY = 5;
  private static final double ELITISM_RATE = 0.15;
  private static final int MAX_GENERATIONS = 2500;
  private static final double RATE_OF_GUESSED_RECURRENCE = 0.5;
  private static final double CROSSOVER_RATIO = 0.1;
  private static final int[] POSSIBLE_PERIODS = new int[]{1, 2, 3, 4, 5, 6, 7};
  private final Recurrences foundRecurrences;

  public GeneticRecurrenceAnalyser(List<EventSample> eventSamples) {
    assertEventSamplesOrdered(eventSamples);
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
    List<Recurrence> guessedRecurrences = new ArrayList<Recurrence>();
    boolean[] halfHourIntervalsWithSamples = findHalfHourIntervalsWithSamples(eventSamples);
    int[] possiblePeriodsInDays = POSSIBLE_PERIODS;
    Calendar timeOfOccurrence = eventSamples.get(0).getTimestampAsCalendar();
    for (int i = 0; i < halfHourIntervalsWithSamples.length; i++) {
      if (halfHourIntervalsWithSamples[i]) {
        for (int periodInDays : possiblePeriodsInDays) {
          for (int dayOfWeek = 1; dayOfWeek <= 7; dayOfWeek++) {
            timeOfOccurrence.set(Calendar.DAY_OF_WEEK, dayOfWeek);
            timeOfOccurrence.set(Calendar.HOUR_OF_DAY, i / 2);
            timeOfOccurrence.set(Calendar.MINUTE, 30 * (i % 2));
            guessedRecurrences.add(new DailyPeriodRecurrence(periodInDays, timeOfOccurrence));
          }
        }
      }
    }
    return guessedRecurrences;
  }

  private static boolean[] findHalfHourIntervalsWithSamples(List<EventSample> eventSamples) {
    boolean[] halfHourIntervals = new boolean[TimeUtils.DAY_IN_HOURS * 2];
    for (EventSample eventSample : eventSamples) {
      Calendar timestamp = eventSample.getTimestampAsCalendar();
      int intervalIndex = timestamp.get(Calendar.HOUR_OF_DAY) * 2 + timestamp.get(Calendar.MINUTE) / 30;
      halfHourIntervals[intervalIndex] = true;
    }
    return halfHourIntervals;
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
