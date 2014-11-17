package si.urbas.chrony.recurrence.analysis;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.ArrayList;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.EventSamplesTestUtils.*;
import static si.urbas.chrony.util.TimeUtils.MINUTE_IN_MILLIS;
import static si.urbas.chrony.util.TimeUtils.toUtcDate;

public class RecurrenceFitnessTest {

  private static final double ZERO_DELTA = 0d;
  private static final int ONE_HOUR_DEVIATION = 1;
  private static final int TWO_DAYS_PERIOD = 2;
  private static final int ONE_DAY_PERIOD = 1;
  private Random randomnessSource;
  private RecurrenceFitness recurrenceFitness;

  @Before
  public void setUp() throws Exception {
    randomnessSource = new Random(4783297L);
    recurrenceFitness = new RecurrenceFitness();
  }

  @Test
  public void fitness_MUST_return_negative_infinity_WHEN_given_no_samples() {
    assertEquals(Double.NEGATIVE_INFINITY, recurrenceFitness.fitness(recurrence(), emptyEventSamples()), ZERO_DELTA);
  }

  @Test
  public void fitness_MUST_return_a_negative_number_WHEN_event_samples_do_match_the_recurrence() {
    ArrayList<EventSample> biDailyEventSamples = towDayPeriodRandomEventSamples();
    assertThat(recurrenceFitness.fitness(recurrence(), biDailyEventSamples), is(lessThan(0d)));
  }

  @Test
  public void fitness_MUST_return_0_WHEN_the_recurrence_matches_event_samples_exactly() {
    DateTime timeOfFirstOccurrence = toUtcDate(2014, 9, 14, 5, 37, 0);
    ArrayList<EventSample> biDailyEventSamples = twoDayPeriodEventSamples(timeOfFirstOccurrence);
    DailyPeriodRecurrence recurrence = createRecurrence(timeOfFirstOccurrence, TWO_DAYS_PERIOD, (long) 0);
    assertEquals(0, recurrenceFitness.fitness(recurrence, biDailyEventSamples), ZERO_DELTA);
  }

  @Test
  public void fitness_MUST_return_a_smaller_number_for_a_recurrence_that_does_not_fit_samples_as_well_as_another_recurrence() {
    DateTime timeOfFirstOccurrence = toUtcDate(2014, 9, 14, 5, 37, 0);
    ArrayList<EventSample> biDailyEventSamples = twoDayPeriodEventSamples(timeOfFirstOccurrence);
    DailyPeriodRecurrence betterRecurrence1 = createRecurrence(timeOfFirstOccurrence, TWO_DAYS_PERIOD, MINUTE_IN_MILLIS);
    DailyPeriodRecurrence worseRecurrence = createRecurrence(timeOfFirstOccurrence, TWO_DAYS_PERIOD, -2 * MINUTE_IN_MILLIS);
    assertThat(
      recurrenceFitness.fitness(worseRecurrence, biDailyEventSamples),
      is(lessThan(recurrenceFitness.fitness(betterRecurrence1, biDailyEventSamples)))
    );
  }

  @Test
  public void fitness_MUST_return_smaller_number_for_a_recurrence_that_is_more_frequent_than_a_perfect_recurrence() {
    DateTime timeOfFirstOccurrence = toUtcDate(2014, 9, 14, 5, 37, 0);
    ArrayList<EventSample> biDailyEventSamples = twoDayPeriodEventSamples(timeOfFirstOccurrence);
    DailyPeriodRecurrence perfectlyFittingRecurrence = createRecurrence(timeOfFirstOccurrence, TWO_DAYS_PERIOD, (long) 0);
    DailyPeriodRecurrence overfittingRecurrence = createRecurrence(timeOfFirstOccurrence, ONE_DAY_PERIOD, (long) 0);
    assertThat(
      recurrenceFitness.fitness(overfittingRecurrence, biDailyEventSamples),
      is(lessThan(recurrenceFitness.fitness(perfectlyFittingRecurrence, biDailyEventSamples)))
    );
  }

  @Test
  public void fitness_MUST_return_smaller_number_for_a_recurrence_that_is_less_frequent_than_a_perfect_recurrence() {
    DateTime timeOfFirstOccurrence = toUtcDate(2014, 9, 14, 5, 37, 0);
    ArrayList<EventSample> biDailyEventSamples = twoDayPeriodEventSamples(timeOfFirstOccurrence);
    DailyPeriodRecurrence perfectlyFittingRecurrence = createRecurrence(timeOfFirstOccurrence, TWO_DAYS_PERIOD, (long) 0);
    DailyPeriodRecurrence overfittingRecurrence = createRecurrence(timeOfFirstOccurrence, 2 * TWO_DAYS_PERIOD, (long) 0);
    assertThat(
      recurrenceFitness.fitness(overfittingRecurrence, biDailyEventSamples),
      is(lessThan(recurrenceFitness.fitness(perfectlyFittingRecurrence, biDailyEventSamples)))
    );
  }

  private ArrayList<EventSample> twoDayPeriodEventSamples(DateTime timeOfFirstOccurrence) {
    return createRecurringEventSamples(TWO_DAYS_PERIOD, 6 * TWO_DAYS_PERIOD, timeOfFirstOccurrence);
  }

  private ArrayList<EventSample> towDayPeriodRandomEventSamples() {
    return createRandomEventSamples(randomnessSource, TWO_DAYS_PERIOD, 6 * TWO_DAYS_PERIOD, ONE_HOUR_DEVIATION, 2014, 9, 14, 5, 37);
  }

  private static DailyPeriodRecurrence createRecurrence(DateTime timeOfFirstOccurrence, int periodInDays, long phaseShiftInMillis) {
    return new DailyPeriodRecurrence(periodInDays, timeOfFirstOccurrence.plus(phaseShiftInMillis));
  }

  private static Recurrence recurrence() {
    return new DailyPeriodRecurrence(1, 0, 1, 1, 0, 0);
  }

}