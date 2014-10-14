package si.urbas.chrony.recurrence.analysis;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.recurrence.test.matchers.DailyPeriodRecurrenceMatcher;
import si.urbas.chrony.recurrence.test.matchers.RecurrenceOccurringCloseToMatcherBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.EventSamplesTestUtils.*;
import static si.urbas.chrony.util.TimeUtils.HOUR_IN_MILLIS;
import static si.urbas.chrony.util.TimeUtils.toUtcTimeInMillis;

public abstract class RecurrenceAnalyserTest {

  private static final int TWO_DAYS = 2;
  private static final int THREE_DAYS = 3;
  private final EventSample eventSampleAtTime1d17h = eventSampleAtTime(DAY_1, HOUR_17);
  private final EventSample eventSampleAtTime2d17h = eventSampleAtTime(DAY_2, HOUR_17);
  private final EventSample eventSampleAtTime3d17h = eventSampleAtTime(DAY_3, HOUR_17);
  private final EventSample eventSampleAtTime8d17h = eventSampleAtTime(DAY_8, HOUR_17);
  private final EventSample eventSampleAtTime10d17h = eventSampleAtTime(DAY_10, HOUR_17);
  private final Matcher<Recurrence> weeklyRecurrence = recurrenceWithPeriodOf(7);
  private final Matcher<Recurrence> dailyRecurrence = recurrenceWithPeriodOf(1);
  private Random randomnessSource;

  @Before
  public void setUp() {
    randomnessSource = new Random(731297);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructor_MUST_throw_an_exception_WHEN_the_list_of_event_samples_is_not_sorted() {
    createRecurrenceAnalyser(asList(eventSampleAtTime2d17h, eventSampleAtTime1d17h));
  }

  @Test
  public void foundRecurrences_MUST_return_an_empty_list_WHEN_given_no_event_samples() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(emptyEventSamples());
    assertThat(recurrenceAnalyser.foundRecurrences().getRecurrences(), is(empty()));
  }

  @Test
  public void foundRecurrences_MUST_return_an_empty_list_WHEN_the_entire_time_span_of_the_event_is_0() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime10d17h, eventSampleAtTime10d17h));
    assertThat(recurrenceAnalyser.foundRecurrences().getRecurrences(), is(empty()));
  }

  @Test
  public void foundRecurrences_MUST_return_an_empty_list_WHEN_given_a_single_event_sample() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime1d17h));
    assertThat(recurrenceAnalyser.foundRecurrences().getRecurrences(), is(empty()));
  }

  @Test
  public void foundRecurrences_MUST_return_a_daily_recurrence_WHEN_given_two_events_a_day_apart() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime1d17h, eventSampleAtTime2d17h));
    assertThat(recurrenceAnalyser.foundRecurrences().getRecurrences(), contains(dailyRecurrence));
  }

  @Test
  public void foundRecurrences_MUST_return_a_weekly_recurrence_WHEN_given_two_events_a_week_apart() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime1d17h, eventSampleAtTime8d17h));
    assertThat(recurrenceAnalyser.foundRecurrences().getRecurrences(), contains(weeklyRecurrence));
  }

  @Test
  public void foundRecurrences_MUST_return_a_recurrence_of_every_second_day_WHEN_given_two_events_that_are_two_days_apart() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime1d17h, eventSampleAtTime3d17h));
    assertThat(recurrenceAnalyser.foundRecurrences().getRecurrences(), contains(recurrenceWithPeriodOf(TWO_DAYS)));
  }

  @Test
  public void foundRecurrences_MUST_return_a_3_day_recurrence_WHEN_given_a_larger_number_of_samples_roughly_three_day_apart() {
    long firstOccurrenceTimeInMillis = toUtcTimeInMillis(2010, 2, 19, 4, 45, 0);
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(createRandomEventSamples(randomnessSource, 3, 10, 1, firstOccurrenceTimeInMillis));
    assertThat(
      recurrenceAnalyser.foundRecurrences().getRecurrences(),
      contains(
        recurrenceOccurringWithin(HOUR_IN_MILLIS).of(firstOccurrenceTimeInMillis).withPeriodOf(THREE_DAYS)
      )
    );
  }

  @Test
  public void foundRecurrences_MUST_return_a_composite_4_and_7_day_recurrence() {
    long startTimeInMillis_4Day = toUtcTimeInMillis(2010, 2, 19, 4, 45, 0);
    long startTimeInMillis_7Day = toUtcTimeInMillis(2010, 2, 20, 14, 17, 0);
    ArrayList<EventSample> eventSamples = createRandomEventSamples(randomnessSource, 4, 35, 1, startTimeInMillis_4Day);
    addRandomEventSamples(eventSamples, randomnessSource, 7, 35, 1, startTimeInMillis_7Day);
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(eventSamples);
    assertThat(
      recurrenceAnalyser.foundRecurrences().getRecurrences(),
      contains(
        recurrenceOccurringWithin(HOUR_IN_MILLIS).of(startTimeInMillis_4Day).withPeriodOf(4),
        recurrenceOccurringWithin(HOUR_IN_MILLIS).of(startTimeInMillis_7Day).withPeriodOf(7)
      )
    );
  }

  private RecurrenceOccurringCloseToMatcherBuilder recurrenceOccurringWithin(long maxDistanceToOccurrence) {
    return new RecurrenceOccurringCloseToMatcherBuilder(maxDistanceToOccurrence);
  }

  protected abstract RecurrenceAnalyser createRecurrenceAnalyser(List<EventSample> eventSamples);

  private static DailyPeriodRecurrenceMatcher recurrenceWithPeriodOf(final int daysApart) {
    return new DailyPeriodRecurrenceMatcher(daysApart);
  }

}
