package si.urbas.chrony.recurrence.analysis;

import org.junit.Before;
import org.junit.Test;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.EventSamplesTestUtils;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.util.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.EventSamplesTestUtils.createRandomEventSamples;
import static si.urbas.chrony.EventSamplesTestUtils.createRecurringEventSamples;
import static si.urbas.chrony.EventSamplesTestUtils.emptyEventSamples;
import static si.urbas.chrony.util.TimeUtils.toUtcCalendar;

public class RecurrenceFitnessTest {

  private static final double ZERO_DELTA = 0d;
  private static final int ONE_HOUR_DEVIATION = 1;
  private static final int TWO_DAYS_PERIOD = 2;
  private static final int THREE_DAYS_PERIOD = 3;
  private Random randomnessSource;

  @Before
  public void setUp() throws Exception {
    randomnessSource = new Random(4783297L);
  }

  @Test
  public void fitness_MUST_return_0_WHEN_given_no_samples() {
    RecurrenceFitness recurrenceFitness = new RecurrenceFitness(recurrence(), emptyEventSamples());
    assertEquals(0, recurrenceFitness.fitness(), ZERO_DELTA);
  }

  @Test
  public void fitness_MUST_return_a_negative_number_WHEN_event_samples_do_match_the_recurrence() {
    ArrayList<EventSample> biDailyEventSamples = createRandomEventSamples(randomnessSource, TWO_DAYS_PERIOD, 3 * TWO_DAYS_PERIOD, ONE_HOUR_DEVIATION, 2014, 9, 14, 5, 37);
    RecurrenceFitness recurrenceFitness = new RecurrenceFitness(recurrence(), biDailyEventSamples);
    assertThat(recurrenceFitness.fitness(), is(lessThan(0d)));
  }

  @Test
  public void fitness_MUST_return_0_WHEN_the_recurrence_matches_event_samples_exactly() {
    Calendar timeOfFirstOccurrence = toUtcCalendar(2014, 9, 14, 5, 37, 0);
    ArrayList<EventSample> biDailyEventSamples = createRecurringEventSamples(TWO_DAYS_PERIOD, 3 * TWO_DAYS_PERIOD, timeOfFirstOccurrence);
    DailyPeriodRecurrence recurrence = new DailyPeriodRecurrence(TWO_DAYS_PERIOD, timeOfFirstOccurrence);

    RecurrenceFitness recurrenceFitness = new RecurrenceFitness(recurrence, biDailyEventSamples);

    assertEquals(0, recurrenceFitness.fitness(), ZERO_DELTA);
  }

  @Test
  public void fitness_MUST_return_a_smaller_number_for_a_recurrence_that_fits_the_samples_worse_than_another_recurrence() {
    Calendar timeOfFirstOccurrence = toUtcCalendar(2014, 9, 14, 5, 37, 0);
    ArrayList<EventSample> biDailyEventSamples = createRecurringEventSamples(TWO_DAYS_PERIOD, 3 * TWO_DAYS_PERIOD, timeOfFirstOccurrence);
    DailyPeriodRecurrence recurrence = new DailyPeriodRecurrence(TWO_DAYS_PERIOD, timeOfFirstOccurrence);
    DailyPeriodRecurrence recurrence = new DailyPeriodRecurrence(TWO_DAYS_PERIOD, timeOfFirstOccurrence);

    RecurrenceFitness recurrenceFitness = new RecurrenceFitness(recurrence, biDailyEventSamples);

    assertEquals(0, recurrenceFitness.fitness(), ZERO_DELTA);
  }

  private static Recurrence recurrence() {
    return new DailyPeriodRecurrence(1, 0, 0, 0, 0, 0);
  }

}