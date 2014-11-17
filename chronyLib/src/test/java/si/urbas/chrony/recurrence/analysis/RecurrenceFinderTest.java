package si.urbas.chrony.recurrence.analysis;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.ArrayList;
import java.util.Random;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.EventSamplesTestUtils.*;
import static si.urbas.chrony.recurrence.test.matchers.RecurrenceMatchers.recurrence;
import static si.urbas.chrony.util.TimeUtils.HOUR_IN_MILLIS;
import static si.urbas.chrony.util.TimeUtils.toUtcTimeInMillis;

public abstract class RecurrenceFinderTest {

  private static final int TWO_DAYS = 2;
  private static final int THREE_DAYS = 3;
  private final EventSample eventSampleAtTime1d17h = eventSampleAtTime(DAY_1, HOUR_17);
  private final EventSample eventSampleAtTime2d17h = eventSampleAtTime(DAY_2, HOUR_17);
  private final EventSample eventSampleAtTime3d17h = eventSampleAtTime(DAY_3, HOUR_17);
  private final EventSample eventSampleAtTime8d17h = eventSampleAtTime(DAY_8, HOUR_17);
  private final EventSample eventSampleAtTime10d17h = eventSampleAtTime(DAY_10, HOUR_17);
  private final Matcher<Recurrence> weeklyRecurrence = recurrence().withPeriodOf(7);
  private final Matcher<Recurrence> dailyRecurrence = recurrence().withPeriodOf(1);
  protected Random randomnessSource;
  private RecurrenceFinder recurrenceFinder;

  @Before
  public void setUp() {
    randomnessSource = new Random(731297);
    recurrenceFinder = createRecurrenceFinder();
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructor_MUST_throw_an_exception_WHEN_the_list_of_event_samples_is_not_sorted() {
    recurrenceFinder.foundRecurrences(asList(eventSampleAtTime2d17h, eventSampleAtTime1d17h));
  }

  @Test
  public void foundRecurrences_MUST_return_an_empty_list_WHEN_given_no_event_samples() {
    assertThat(recurrenceFinder.foundRecurrences(emptyEventSamples()), is(empty()));
  }

  @Test
  public void foundRecurrences_MUST_return_an_empty_list_WHEN_the_entire_time_span_of_the_event_is_0() {
    assertThat(recurrenceFinder.foundRecurrences(asList(eventSampleAtTime10d17h, eventSampleAtTime10d17h)), is(empty()));
  }

  @Test
  public void foundRecurrences_MUST_return_an_empty_list_WHEN_given_a_single_event_sample() {
    assertThat(recurrenceFinder.foundRecurrences(asList(eventSampleAtTime1d17h)), is(empty()));
  }

  @Test
  public void foundRecurrences_MUST_return_a_daily_recurrence_WHEN_given_two_events_a_day_apart() {
    assertThat(recurrenceFinder.foundRecurrences(asList(eventSampleAtTime1d17h, eventSampleAtTime2d17h)), contains(dailyRecurrence));
  }

  @Test
  public void foundRecurrences_MUST_return_a_weekly_recurrence_WHEN_given_two_events_a_week_apart() {
    assertThat(recurrenceFinder.foundRecurrences(asList(eventSampleAtTime1d17h, eventSampleAtTime8d17h)), contains(weeklyRecurrence));
  }

  @Test
  public void foundRecurrences_MUST_return_a_recurrence_of_every_second_day_WHEN_given_two_events_that_are_two_days_apart() {
    assertThat(recurrenceFinder.foundRecurrences(asList(eventSampleAtTime1d17h, eventSampleAtTime3d17h)), contains(recurrence().withPeriodOf(TWO_DAYS)));
  }

  @Test
  public void foundRecurrences_MUST_return_a_3_day_recurrence_WHEN_given_a_larger_number_of_samples_roughly_three_day_apart() {
    long firstOccurrenceTimeInMillis = toUtcTimeInMillis(2010, 2, 19, 4, 45, 0);
    ArrayList<EventSample> eventSamples = createRandomEventSamples(randomnessSource, 3, 30, 1, firstOccurrenceTimeInMillis);
    assertThat(
      recurrenceFinder.foundRecurrences(eventSamples),
      contains(
        recurrence().within(HOUR_IN_MILLIS).of(firstOccurrenceTimeInMillis).withPeriodOf(THREE_DAYS)
      )
    );
  }

  @Test
  @Ignore
  public void foundRecurrences_MUST_return_a_composite_4_and_7_day_recurrence() {
    long startTimeInMillis_4Day = toUtcTimeInMillis(2010, 2, 19, 4, 45, 0);
    long startTimeInMillis_7Day = toUtcTimeInMillis(2010, 2, 20, 14, 17, 0);
    ArrayList<EventSample> eventSamples = createRandomEventSamples(randomnessSource, 4, 35, 1, startTimeInMillis_4Day);
    addRandomEventSamples(eventSamples, randomnessSource, 7, 35, 1, startTimeInMillis_7Day);
    assertThat(
      recurrenceFinder.foundRecurrences(eventSamples),
      contains(
        recurrence().within(HOUR_IN_MILLIS).of(startTimeInMillis_4Day).withPeriodOf(4),
        recurrence().within(HOUR_IN_MILLIS).of(startTimeInMillis_7Day).withPeriodOf(7)
      )
    );
  }

  protected abstract RecurrenceFinder createRecurrenceFinder();

}
