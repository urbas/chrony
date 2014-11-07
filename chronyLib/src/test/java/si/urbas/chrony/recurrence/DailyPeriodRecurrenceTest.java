package si.urbas.chrony.recurrence;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static si.urbas.chrony.util.TimeUtils.*;

public class DailyPeriodRecurrenceTest {

  private static final int PERIOD_1_DAY = 1;
  private static final int PERIOD_3_DAYS = 3;
  private static final int PERIOD_7_DAYS = 7;
  private static final long RANGE_WIDTH_10_MILLIS = 10;

  @Test
  public void distanceTo_MUST_return_0_WHEN_the_nearest_recurrence_happens_exactly_on_the_given_time() {
    DateTime firstOccurrence = createUtcDate();
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_1_DAY, firstOccurrence);
    assertEquals(0, dailyPeriodRecurrence.distanceTo(firstOccurrence.getMillis()));
  }

  @Test
  public void distanceTo_MUST_return_60000_WHEN_the_nearest_recurrence_happens_one_minute_before_the_given_time() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_1_DAY, 2000, 3, 14, 15, 32);
    long timeOfInterest = toUtcTimeInMillis(2000, 3, 14, 15, 33, 0);
    assertEquals(60000, dailyPeriodRecurrence.distanceTo(timeOfInterest));
  }

  @Test
  public void distanceTo_MUST_return_negative_120000_WHEN_the_nearest_recurrence_happens_2_minutes_after_the_given_time() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_1_DAY, 2000, 3, 14, 15, 33);
    long timeOfInterest = toUtcTimeInMillis(2000, 3, 14, 15, 31, 0);
    assertEquals(-120000, dailyPeriodRecurrence.distanceTo(timeOfInterest));
  }

  @Test
  public void distanceTo_MUST_return_negative_120000_WHEN_some_future_recurrence_happens_2_minutes_after_the_given_time() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_1_DAY, 2000, 3, 14, 15, 33);
    long timeOfInterest = toUtcTimeInMillis(2000, 4, 28, 15, 31, 0);
    assertEquals(-120000, dailyPeriodRecurrence.distanceTo(timeOfInterest));
  }

  @Test
  public void distanceTo_MUST_return_180000_WHEN_some_future_occurrence_happens_3_minutes_before_the_given_time() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_3_DAYS, 2000, 3, 1, 10, 10);
    long timeOfInterest = toUtcTimeInMillis(2000, 3, 13, 10, 13, 0);
    assertEquals(180000, dailyPeriodRecurrence.distanceTo(timeOfInterest));
  }

  @Test
  public void distanceTo_MUST_return_180000_WHEN_some_past_occurrence_happens_3_minutes_before_the_given_time() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_3_DAYS, 2000, 3, 13, 10, 10);
    long timeOfInterest = toUtcTimeInMillis(2000, 3, 10, 10, 13, 0);
    assertEquals(180000, dailyPeriodRecurrence.distanceTo(timeOfInterest));
  }

  @Test
  public void distanceTo_MUST_return_180000_WHEN_some_far_future_occurrence_happens_3_minutes_before_the_given_time() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_3_DAYS, 2000, 3, 1, 10, 10);
    long timeOfInterest = toUtcTimeInMillis(2014, 3, 12, 10, 13, 0);
    assertEquals(180000, dailyPeriodRecurrence.distanceTo(timeOfInterest));
  }

  @Test
  public void distanceTo_MUST_return_180000_WHEN_some_far_past_occurrence_happens_3_minutes_before_the_given_time() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_3_DAYS, 2000, 3, 1, 10, 10);
    long timeOfInterest = toUtcTimeInMillis(1985, 3, 2, 10, 13, 0);
    assertEquals(180000, dailyPeriodRecurrence.distanceTo(timeOfInterest));
  }

  @Test
  public void distanceTo_MUST_return_5h_30min() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_7_DAYS, 2014, 8, 29, 16, 10);
    long timeOfInterest = toUtcTimeInMillis(2014, 7, 4, 21, 40, 0);
    long distanceOf5h30min = 5 * HOUR_IN_MILLIS + 30 * MINUTE_IN_MILLIS;
    long calculatedDistance = dailyPeriodRecurrence.distanceTo(timeOfInterest);
    assertEquals(distanceOf5h30min, calculatedDistance);
  }

  @Test
  public void distanceTo_MUST_return_negative_5h_30min() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_7_DAYS, 2014, 7, 4, 21, 40);
    long timeOfInterest = toUtcTimeInMillis(2014, 8, 29, 16, 10, 0);
    long negativeDistance5h30min = -(5 * HOUR_IN_MILLIS + 30 * MINUTE_IN_MILLIS);
    long calculatedDistance = dailyPeriodRecurrence.distanceTo(timeOfInterest);
    assertEquals(negativeDistance5h30min, calculatedDistance);
  }

  @Test
  public void distanceTo_MUST_return_3days_11h_29min() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_7_DAYS, 2014, 8, 1, 11, 30);
    long timeOfInterest = toUtcTimeInMillis(2014, 8, 18, 22, 59, 0);
    long negativeDistance5h30min = 3 * DAY_IN_MILLIS + 11 * HOUR_IN_MILLIS + 29 * MINUTE_IN_MILLIS;
    long calculatedDistance = dailyPeriodRecurrence.distanceTo(timeOfInterest);
    assertEquals(negativeDistance5h30min, calculatedDistance);
  }

  @Test
  public void distanceTo_MUST_return_negative_3days_11h_29min() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_7_DAYS, 2014, 8, 1, 11, 30);
    long timeOfInterest = toUtcTimeInMillis(2014, 8, 19, 0, 1, 0);
    long negativeDistance5h30min = -(3 * DAY_IN_MILLIS + 11 * HOUR_IN_MILLIS + 29 * MINUTE_IN_MILLIS);
    long calculatedDistance = dailyPeriodRecurrence.distanceTo(timeOfInterest);
    assertEquals(negativeDistance5h30min, calculatedDistance);
  }

  @Test
  public void equals_MUST_return_true_for_two_instances_created_with_same_parameters() {
    assertEquals(createDailyPeriodRecurrence(), createDailyPeriodRecurrence());
  }

  @Test
  public void equals_MUST_return_false_for_two_instances_created_with_different_parameters() {
    assertNotEquals(new DailyPeriodRecurrence(1, 2, 3, 4, 0, 6), createDailyPeriodRecurrence());
  }

  @Test
  public void getOccurrencesBetween_MUST_return_an_empty_list_WHEN_the_range_is_non_positive() {
    List<Long> foundOccurrences = createDailyPeriodRecurrence().subOccurrences(123, 123);
    assertThat(foundOccurrences, is(empty()));
  }

  @Test
  public void getOccurrencesBetween_MUST_return_one_occurrence_WHEN_the_range_includes_the_initial_occurrence() {
    DateTime firstOccurrence = toUtcDate(1, 2, 3, 4, 5, 6);
    long rangeStart = firstOccurrence.getMillis() - RANGE_WIDTH_10_MILLIS / 2;
    DailyPeriodRecurrence recurrence = new DailyPeriodRecurrence(PERIOD_1_DAY, firstOccurrence);
    List<Long> foundOccurrences = recurrence.subOccurrences(rangeStart, rangeStart + RANGE_WIDTH_10_MILLIS);
    assertThat(foundOccurrences, contains(firstOccurrence.getMillis()));
  }

  @Test
  public void getOccurrencesBetween_MUST_return_a_future_occurrence_WHEN_the_range_includes_a_future_occurrence() {
    DateTime firstOccurrence = toUtcDate(1, 2, 3, 4, 5, 6);
    long futureOccurrence = toUtcTimeInMillis(1, 2, 5, 4, 5, 6);
    long rangeStart = futureOccurrence - RANGE_WIDTH_10_MILLIS / 2;
    DailyPeriodRecurrence recurrence = new DailyPeriodRecurrence(PERIOD_1_DAY, firstOccurrence);
    List<Long> foundOccurrences = recurrence.subOccurrences(rangeStart, rangeStart + RANGE_WIDTH_10_MILLIS);
    assertThat(foundOccurrences, contains(futureOccurrence));
  }

  @Test
  public void getOccurrencesBetween_MUST_return_past_and_future_occurrences_WHEN_the_range_includes_them() {
    DateTime firstOccurrence = toUtcDate(2014, 8, 22, 13, 45, 0);
    long rangeStart = toUtcTimeInMillis(2014, 8, 8, 11, 45, 0);
    long rangeEnd = toUtcTimeInMillis(2014, 9, 6, 19, 45, 0);
    DailyPeriodRecurrence recurrence = new DailyPeriodRecurrence(PERIOD_7_DAYS, firstOccurrence);
    Occurrences foundOccurrences = recurrence.subOccurrences(rangeStart, rangeEnd);
    assertThat(
      foundOccurrences,
      contains(
        toUtcTimeInMillis(2014, 8, 8, 13, 45, 0),
        toUtcTimeInMillis(2014, 8, 15, 13, 45, 0),
        toUtcTimeInMillis(2014, 8, 22, 13, 45, 0),
        toUtcTimeInMillis(2014, 8, 29, 13, 45, 0),
        toUtcTimeInMillis(2014, 9, 5, 13, 45, 0)
      )
    );
  }

  private static DailyPeriodRecurrence createDailyPeriodRecurrence() {return new DailyPeriodRecurrence(1, 2, 3, 4, 5, 6);}
}