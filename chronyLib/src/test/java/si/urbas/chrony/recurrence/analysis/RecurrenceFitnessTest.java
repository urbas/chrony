package si.urbas.chrony.recurrence.analysis;

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
import static si.urbas.chrony.util.TimeUtils.*;

public class RecurrenceFitnessTest {

  private static final double ZERO_DELTA = 0d;
  private static final int ONE_HOUR_DEVIATION = 1;
  private static final int TWO_DAYS_PERIOD = 2;
  private static final int ONE_DAY_PERIOD = 1;
  private Random randomnessSource;

  @Before
  public void setUp() throws Exception {
    randomnessSource = new Random(4783297L);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructor_MUST_throw_an_exception_WHEN_the_list_of_event_samples_is_not_sorted() {
    ArrayList<EventSample> eventSamples = twoDayPeriodEventSamples(toUtcTimeInMillis(2014, 9, 14, 5, 37, 0));
    eventSamples.add(0, eventSamples.get(2));
    new RecurrenceFitness(null, eventSamples);
  }

  @Test
  public void fitness_MUST_return_negative_infinity_WHEN_given_no_samples() {
    RecurrenceFitness recurrenceFitness = new RecurrenceFitness(recurrence(), emptyEventSamples());
    assertEquals(Double.NEGATIVE_INFINITY, recurrenceFitness.fitness(), ZERO_DELTA);
  }

  @Test
  public void fitness_MUST_return_a_negative_number_WHEN_event_samples_do_match_the_recurrence() {
    ArrayList<EventSample> biDailyEventSamples = towDayPeriodRandomEventSamples();
    RecurrenceFitness recurrenceFitness = new RecurrenceFitness(recurrence(), biDailyEventSamples);
    assertThat(recurrenceFitness.fitness(), is(lessThan(0d)));
  }

  @Test
  public void fitness_MUST_return_0_WHEN_the_recurrence_matches_event_samples_exactly() {
    long timeOfFirstOccurrence = toUtcTimeInMillis(2014, 9, 14, 5, 37, 0);
    ArrayList<EventSample> biDailyEventSamples = twoDayPeriodEventSamples(timeOfFirstOccurrence);
    RecurrenceFitness recurrenceFitness = createRecurrenceFitness(timeOfFirstOccurrence, TWO_DAYS_PERIOD, biDailyEventSamples, 0);
    assertEquals(0, recurrenceFitness.fitness(), ZERO_DELTA);
  }

  @Test
  public void fitness_MUST_return_a_smaller_number_for_a_recurrence_that_does_not_fit_samples_as_well_as_another_recurrence() {
    long timeOfFirstOccurrence = toUtcTimeInMillis(2014, 9, 14, 5, 37, 0);
    ArrayList<EventSample> biDailyEventSamples = twoDayPeriodEventSamples(timeOfFirstOccurrence);
    RecurrenceFitness betterRecurrenceFitness = createRecurrenceFitness(timeOfFirstOccurrence, TWO_DAYS_PERIOD, biDailyEventSamples, MINUTE_IN_MILLIS);
    RecurrenceFitness worseRecurrenceFitness = createRecurrenceFitness(timeOfFirstOccurrence, TWO_DAYS_PERIOD, biDailyEventSamples, -2 * MINUTE_IN_MILLIS);
    assertThat(worseRecurrenceFitness.fitness(), is(lessThan(betterRecurrenceFitness.fitness())));
  }

  @Test
  public void fitness_MUST_return_smaller_number_for_a_recurrence_that_is_more_frequent_than_a_perfect_recurrence() {
    long timeOfFirstOccurrence = toUtcTimeInMillis(2014, 9, 14, 5, 37, 0);
    ArrayList<EventSample> biDailyEventSamples = twoDayPeriodEventSamples(timeOfFirstOccurrence);
    RecurrenceFitness perfectRecurrenceFitness = createRecurrenceFitness(timeOfFirstOccurrence, TWO_DAYS_PERIOD, biDailyEventSamples, 0);
    RecurrenceFitness overfittingRecurrenceFitness = createRecurrenceFitness(timeOfFirstOccurrence, ONE_DAY_PERIOD, biDailyEventSamples, 0);
    assertThat(overfittingRecurrenceFitness.fitness(), is(lessThan(perfectRecurrenceFitness.fitness())));
  }

  @Test
  public void fitness_MUST_return_smaller_number_for_a_recurrence_that_is_less_frequent_than_a_perfect_recurrence() {
    long timeOfFirstOccurrence = toUtcTimeInMillis(2014, 9, 14, 5, 37, 0);
    ArrayList<EventSample> biDailyEventSamples = twoDayPeriodEventSamples(timeOfFirstOccurrence);
    RecurrenceFitness perfectRecurrenceFitness = createRecurrenceFitness(timeOfFirstOccurrence, TWO_DAYS_PERIOD, biDailyEventSamples, 0);
    RecurrenceFitness overfittingRecurrenceFitness = createRecurrenceFitness(timeOfFirstOccurrence, 2 * TWO_DAYS_PERIOD, biDailyEventSamples, 0);
    assertThat(overfittingRecurrenceFitness.fitness(), is(lessThan(perfectRecurrenceFitness.fitness())));
  }

  private ArrayList<EventSample> twoDayPeriodEventSamples(long timeOfFirstOccurrence) {
    return createRecurringEventSamples(TWO_DAYS_PERIOD, 6 * TWO_DAYS_PERIOD, toUtcCalendar(timeOfFirstOccurrence));
  }

  private ArrayList<EventSample> towDayPeriodRandomEventSamples() {
    return createRandomEventSamples(randomnessSource, TWO_DAYS_PERIOD, 6 * TWO_DAYS_PERIOD, ONE_HOUR_DEVIATION, 2014, 9, 14, 5, 37);
  }

  private static RecurrenceFitness createRecurrenceFitness(long timeOfFirstOccurrence, int periodInDays, ArrayList<EventSample> eventSamples, long phaseShiftInMillis) {
    DailyPeriodRecurrence betterRecurrence = new DailyPeriodRecurrence(periodInDays, toUtcCalendar(timeOfFirstOccurrence + phaseShiftInMillis));
    return new RecurrenceFitness(betterRecurrence, eventSamples);
  }

  private static Recurrence recurrence() {
    return new DailyPeriodRecurrence(1, 0, 0, 0, 0, 0);
  }

}