package si.urbas.chrony.recurrence.analysis;

import org.junit.Test;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.recurrence.Recurrences;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.EventSamplesTestUtils.*;
import static si.urbas.chrony.recurrence.RecurrencesTestUtils.emptyRecurrences;
import static si.urbas.chrony.recurrence.RecurrencesTestUtils.recurrences;

public class RecurrenceFitnessPolicyTest {

  private static final int COMPLETELY_PRECISE = 0;

  @Test
  public void fitness_MUST_return_0_WHEN_given_no_samples_and_empty_recurrence() {
    RecurrenceFitnessPolicy fitnessPolicy = new RecurrenceFitnessPolicy(emptyEventSamples());
    double actualFitness = fitnessPolicy.fitness(emptyRecurrences());
    int expectedFitness = 0;
    assertEquals(expectedFitness, actualFitness, COMPLETELY_PRECISE);
  }

  @Test
  public void fitness_MUST_severely_punish_non_empty_recurrences_WHEN_given_less_than_two_samples() {
    RecurrenceFitnessPolicy fitnessPolicy = new RecurrenceFitnessPolicy(asList(eventSampleAtTime(1, 0)));
    double actualFitness = fitnessPolicy.fitness(recurrences(createTestRecurrence()));
    double expectedFitness = Double.NEGATIVE_INFINITY;
    assertEquals(expectedFitness, actualFitness, COMPLETELY_PRECISE);
  }

  @Test
  public void fitness_MUST_return_a_negative_number_WHEN_given_some_recurrences_AND_no_samples() {
    RecurrenceFitnessPolicy fitnessPolicy = new RecurrenceFitnessPolicy(emptyEventSamples());
    assertThat(fitnessPolicy.fitness(singleDailyRecurrence()), is(lessThan(0.0)));
  }

  @Test
  public void fitness_MUST_favour_non_empty_recurrences_WHEN_there_are_some_samples() {
    RecurrenceFitnessPolicy fitnessPolicy = new RecurrenceFitnessPolicy(asList(eventSampleAtTime(1, 0), eventSampleAtTime(2, 0)));
    assertThat(
      fitnessPolicy.fitness(emptyRecurrences()),
      is(lessThan(fitnessPolicy.fitness(singleDailyRecurrence())))
    );
  }

  @Test
  public void fitness_MUST_return_a_smaller_number_FOR_a_recurrence_with_a_wrong_period_THAN_a_recurrence_that_perfectly_matches_the_samples() {
    RecurrenceFitnessPolicy fitnessPolicy = new RecurrenceFitnessPolicy(asList(eventSampleAtTime(1, 0), eventSampleAtTime(2, 0)));
    assertThat(
      fitnessPolicy.fitness(singleWeeklyRecurrence()),
      is(lessThan(fitnessPolicy.fitness(singleDailyRecurrence())))
    );
  }

  @Test
  public void fitness_MUST_return_a_smaller_number_FOR_a_recurrence_that_prescribes_fewer_spurious_occurrences() {
    RecurrenceFitnessPolicy fitnessPolicy = new RecurrenceFitnessPolicy(asList(eventSampleAtTime(7, 0), eventSampleAtTime(14, 0)));
    assertThat(
      fitnessPolicy.fitness(singleDailyRecurrence()),
      is(lessThan(fitnessPolicy.fitness(singleWeeklyRecurrence())))
    );
  }

  @Test
  public void fitness_MUST_prefer_two_recurrences_to_one_WHEN_two_recurrences_match_the_samples_perfectly() {
    RecurrenceFitnessPolicy fitnessPolicy = new RecurrenceFitnessPolicy(asList(eventSampleAtTime(1, 0), eventSampleAtTime(2, 0), eventSampleAtTime(8, 0), eventSampleAtTime(9, 0)));
    assertThat(
      fitnessPolicy.fitness(recurrences(new DailyPeriodRecurrence(7, 0, 0, 1, 0, 0))),
      is(lessThan(fitnessPolicy.fitness(recurrences(new DailyPeriodRecurrence(7, 0, 0, 1, 0, 0), new DailyPeriodRecurrence(7, 0, 0, 2, 0, 0)))))
    );
  }

  @Test
  public void fitness_MUST_return_the_smallest_number_for_the_daily_recurrence_WHEN_given_random_and_roughly_daily_samples() {
    int periodInDays = 1;
    int durationInDays = 5;
    int maxDeviationInHours = 1;
    ArrayList<EventSample> roughlyDailySamples = createRandomEventSamples(periodInDays, durationInDays, maxDeviationInHours, 2014, 8, 16, 14, 37);
    RecurrenceFitnessPolicy fitnessPolicy = new RecurrenceFitnessPolicy(roughlyDailySamples);
    assertThat(
      fitnessPolicy.fitness(recurrences(createTestRecurrence())),
      is(lessThan(fitnessPolicy.fitness(recurrences(new DailyPeriodRecurrence(periodInDays, 2014, 8, 16, 14, 37)))))
    );
  }

  @Test
  public void fitness_MUST_return_the_smallest_number_for_the_daily_recurrence_WHEN_two_occurrences_are_missing_in_seven_days() {
    RecurrenceFitnessPolicy fitnessPolicy = prepareFitnessPolicyWith5DailyRandomisedSamples();
    assertThat(
      fitnessPolicy.fitness(recurrences(createTestRecurrence())),
      is(lessThan(fitnessPolicy.fitness(recurrences(new DailyPeriodRecurrence(1, 2014, 8, 16, 14, 37)))))
    );
  }

  @Test
  public void fitness_MUST_prefer_one_recurrence_WHEN_two_two_cover_exactly_the_same() {
    RecurrenceFitnessPolicy fitnessPolicy = prepareFitnessPolicyWithBidailyRandomisedSamples();
    assertThat(
      fitnessPolicy.fitness(recurrences(new DailyPeriodRecurrence(2, 2014, 8, 16, 14, 37), new DailyPeriodRecurrence(2, 2014, 8, 17, 14, 37))),
      is(lessThan(fitnessPolicy.fitness(recurrences(new DailyPeriodRecurrence(1, 2010, 8, 16, 14, 37)))))
    );
  }

  private static Recurrences singleWeeklyRecurrence() {
    return recurrences(new DailyPeriodRecurrence(7, 0, 0, 0, 0, 0));
  }

  private static Recurrences singleDailyRecurrence() {
    return recurrences(new DailyPeriodRecurrence(1, 0, 0, 0, 0, 0));
  }

  private static RecurrenceFitnessPolicy prepareFitnessPolicyWith5DailyRandomisedSamples() {
    int durationInDays = 7;
    int maxDeviationInHours = 1;
    ArrayList<EventSample> roughlyDailySamples = createRandomEventSamples(1, durationInDays, maxDeviationInHours, 2014, 8, 16, 14, 37);
    roughlyDailySamples.remove(2);
    roughlyDailySamples.remove(2);
    return new RecurrenceFitnessPolicy(roughlyDailySamples);
  }

  private static RecurrenceFitnessPolicy prepareFitnessPolicyWithBidailyRandomisedSamples() {
    int durationInDays = 17;
    int maxDeviationInHours = 1;
    ArrayList<EventSample> samples = createRandomEventSamples(2, durationInDays, maxDeviationInHours, 2014, 8, 16, 14, 37);
    return new RecurrenceFitnessPolicy(samples);
  }

  private static Recurrence createTestRecurrence() {return new DailyPeriodRecurrence(7, 2014, 8, 16, 14, 37);}

}