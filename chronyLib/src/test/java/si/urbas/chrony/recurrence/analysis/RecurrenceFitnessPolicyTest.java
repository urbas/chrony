package si.urbas.chrony.recurrence.analysis;

import org.junit.Test;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrences;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.EventSamplesTestUtils.emptyEventSamples;
import static si.urbas.chrony.EventSamplesTestUtils.eventSampleAtTime;
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
  public void fitness_MUST_return_a_negative_number_WHEN_given_some_recurrences_AND_no_samples() {
    RecurrenceFitnessPolicy fitnessPolicy = new RecurrenceFitnessPolicy(emptyEventSamples());
    assertThat(fitnessPolicy.fitness(singleDailyRecurrence()), is(lessThan(0.0)));
  }

  @Test
  public void fitness_MUST_return_a_smaller_number_FOR_a_recurrence_with_a_wrong_period_THAN_a_recurrence_that_perfectly_matches_the_samples() {
    RecurrenceFitnessPolicy fitnessPolicy = new RecurrenceFitnessPolicy(asList(eventSampleAtTime(1, 0), eventSampleAtTime(2, 0)));
    assertThat(
      fitnessPolicy.fitness(singleWeeklyRecurrence()),
      is(lessThan(fitnessPolicy.fitness(singleDailyRecurrence())))
    );
  }

  private static Recurrences singleWeeklyRecurrence() {
    return recurrences(new DailyPeriodRecurrence(2, 0, 0, 0, 0, 0));
  }

  private static Recurrences singleDailyRecurrence() {
    return recurrences(new DailyPeriodRecurrence(1, 0, 0, 0, 0, 0));
  }

}