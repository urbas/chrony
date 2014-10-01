package si.urbas.chrony.recurrence.analysis;

import org.junit.Test;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.EventSamplesTestUtils;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrences;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.EventSamplesTestUtils.emptyEventSamples;
import static si.urbas.chrony.EventSamplesTestUtils.eventSampleAtTime;
import static si.urbas.chrony.recurrence.RecurrencesTestUtils.emptyRecurrences;
import static si.urbas.chrony.recurrence.RecurrencesTestUtils.recurrences;
import static si.urbas.chrony.util.TimeUtils.*;

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
      fitnessPolicy.fitness(recurrences(new DailyPeriodRecurrence(7, 2014, 8, 16, 14, 37))),
      is(lessThan(fitnessPolicy.fitness(recurrences(new DailyPeriodRecurrence(periodInDays, 2014, 8, 16, 14, 37)))))
    );
  }

  @Test
  public void fitness_MUST_return_the_smallest_number_for_the_daily_recurrence_WHEN_two_occurrences_are_missing_in_seven_days() {
    int periodInDays = 1;
    int durationInDays = 7;
    int maxDeviationInHours = 1;
    ArrayList<EventSample> roughlyDailySamples = createRandomEventSamples(periodInDays, durationInDays, maxDeviationInHours, 2014, 8, 16, 14, 37);
    roughlyDailySamples.remove(2);
    roughlyDailySamples.remove(2);
    RecurrenceFitnessPolicy fitnessPolicy = new RecurrenceFitnessPolicy(roughlyDailySamples);
    assertThat(
      fitnessPolicy.fitness(recurrences(new DailyPeriodRecurrence(7, 2014, 8, 16, 14, 37))),
      is(lessThan(fitnessPolicy.fitness(recurrences(new DailyPeriodRecurrence(periodInDays, 2014, 8, 16, 14, 37)))))
    );
  }

  private long randomValueBetween(long rangeStart, long rangeEnd) {
    return Math.round(Math.random() * (rangeEnd - rangeStart) + rangeStart);
  }

  private static Recurrences singleWeeklyRecurrence() {
    return recurrences(new DailyPeriodRecurrence(7, 0, 0, 0, 0, 0));
  }

  private static Recurrences singleDailyRecurrence() {
    return recurrences(new DailyPeriodRecurrence(1, 0, 0, 0, 0, 0));
  }

  private ArrayList<EventSample> createRandomEventSamples(int periodInDays, int durationInDays, int maxDeviationInHours, int year, int month, int dayOfMonth, int hourOfDay, int minutesPastHour) {
    ArrayList<EventSample> roughlyDailySamples = new ArrayList<EventSample>();
    long startTimeInMillis = toUtcTimeInMillis(year, month, dayOfMonth, hourOfDay, minutesPastHour, 0);
    long endTimeInMillis = startTimeInMillis + durationInDays * DAY_IN_MILLIS;
    long maxDeviationFromExactRecurrence = maxDeviationInHours * HOUR_IN_MILLIS;
    addUniformlyRandomOccurrences(roughlyDailySamples, periodInDays, startTimeInMillis, endTimeInMillis, maxDeviationFromExactRecurrence);
    return roughlyDailySamples;
  }

  private void addUniformlyRandomOccurrences(ArrayList<EventSample> samplesToAddTo, long periodInDays, long startTimeInMillis, long endTimeInMillis, long maxDeviationFromExactRecurrence) {
    long periodInMillis = periodInDays * DAY_IN_MILLIS;
    for (long currentOccurrence = startTimeInMillis; currentOccurrence < endTimeInMillis; currentOccurrence += periodInMillis) {
      samplesToAddTo.add(EventSamplesTestUtils.eventSampleAtTime(currentOccurrence + randomValueBetween(-maxDeviationFromExactRecurrence, maxDeviationFromExactRecurrence)));
    }
  }

}