package si.urbas.chrony.recurrence.analysis;

import org.junit.Test;
import si.urbas.chrony.recurrence.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.*;
import static si.urbas.chrony.EventSamplesTestUtils.emptyEventSamples;
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
  public void fitness_MUST_return_a_negative_number_FOR_a_single_daily_recurrence_WHEN_given_no_samples() {
    RecurrenceFitnessPolicy fitnessPolicy = new RecurrenceFitnessPolicy(emptyEventSamples());
    assertThat(fitnessPolicy.fitness(singleDailyRecurrence()), is(lessThan(0.0)));
  }

  private Recurrences singleDailyRecurrence() {return recurrences(new DailyPeriodRecurrence(1, 0, 0));}

}