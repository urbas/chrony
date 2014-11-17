package si.urbas.chrony.recurrence.analysis;

import org.junit.Before;
import org.junit.Test;
import si.urbas.chrony.EventSample;

import java.util.ArrayList;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.EventSamplesTestUtils.*;
import static si.urbas.chrony.recurrence.test.matchers.RecurrenceMatchers.recurrence;
import static si.urbas.chrony.util.TimeUtils.*;

public class SingleDailyRecurrenceFinderTest {

  private static final int THREE_DAYS = 3;
  private static final long TIME_OF_FIRST_OCCURRENCE = toUtcTimeInMillis(2014, 9, 19, 18, 12, 0);
  private static final long ONE_HOUR_DEVIATION = HOUR_IN_MILLIS;
  private Random random;
  private RecurrenceFinder recurrenceFinder;

  @Before
  public void setUp() {
    recurrenceFinder = new SingleDailyRecurrenceFinder();
    random = new Random(9382937L);
  }

  @Test
  public void foundRecurrences_MUST_be_empty_WHEN_there_are_fewer_than_two_event_samples() {
    assertThat(recurrenceFinder.foundRecurrences(emptyEventSamples()), is(empty()));
  }

  @Test
  public void foundRecurrences_MUST_return_a_recurrence_for_two_events() {
    ArrayList<EventSample> twoEventSamples = createRecurringEventSamples(THREE_DAYS, THREE_DAYS, TIME_OF_FIRST_OCCURRENCE);
    assertThat(
      recurrenceFinder.foundRecurrences(twoEventSamples),
      contains(recurrence().withPeriodOf(THREE_DAYS))
    );
  }

  @Test
  public void foundRecurrences_MUST_have_a_period_that_is_close_to_the_average_period() {
    ArrayList<EventSample> eventSamples = createRandomEventSamples(random, THREE_DAYS, 10 * THREE_DAYS, ONE_HOUR_DEVIATION, TIME_OF_FIRST_OCCURRENCE);
    assertThat(
      recurrenceFinder.foundRecurrences(eventSamples),
      contains(recurrence().withPeriodOf(THREE_DAYS))
    );
  }

  @Test
  public void foundRecurrences_MUST_occur_at_a_time_of_day_that_is_close_to_the_average_of_all_events() {
    ArrayList<EventSample> eventSamples = createRandomEventSamples(random, THREE_DAYS, 30 * THREE_DAYS, ONE_HOUR_DEVIATION, TIME_OF_FIRST_OCCURRENCE);
    assertThat(
      recurrenceFinder.foundRecurrences(eventSamples),
      contains(recurrence().within(10 * MINUTE_IN_MILLIS).of(TIME_OF_FIRST_OCCURRENCE).withPeriodOf(THREE_DAYS))
    );
  }

}