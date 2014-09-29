package si.urbas.chrony.recurrence;

import org.junit.Test;

import java.util.Calendar;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.util.TimeUtils.*;

public class DailyPeriodRecurrenceTest {

  private static final int PERIOD_1_DAY = 1;
  private static final int PERIOD_3_DAYS = 3;
  private static final int PERIOD_7_DAYS = 7;

  @Test
  public void distanceTo_MUST_return_0_WHEN_the_nearest_recurrence_happens_exactly_on_the_given_time() {
    Calendar calendar = createUtcCalendar();
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_1_DAY, calendar);
    assertEquals(0, dailyPeriodRecurrence.distanceTo(calendar.getTimeInMillis()));
  }

  @Test
  public void distanceTo_MUST_return_60000_WHEN_the_nearest_recurrence_happens_one_minute_before_the_given_time() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_1_DAY, 2000, 3, 14, 15, 32);
    Calendar timeOfInterest = toUtcCalendar(2000, 3, 14, 15, 33, 0);
    assertEquals(60000, dailyPeriodRecurrence.distanceTo(timeOfInterest.getTimeInMillis()));
  }

  @Test
  public void distanceTo_MUST_return_negative_120000_WHEN_the_nearest_recurrence_happens_2_minutes_after_the_given_time() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_1_DAY, 2000, 3, 14, 15, 33);
    Calendar timeOfInterest = toUtcCalendar(2000, 3, 14, 15, 31, 0);
    assertEquals(-120000, dailyPeriodRecurrence.distanceTo(timeOfInterest.getTimeInMillis()));
  }

  @Test
  public void distanceTo_MUST_return_negative_120000_WHEN_some_future_recurrence_happens_2_minutes_after_the_given_time() {
    System.out.println("Past:");
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_1_DAY, 2000, 3, 14, 15, 33);
    Calendar timeOfInterest = toUtcCalendar(2000, 4, 28, 15, 31, 0);
    assertEquals(-120000, dailyPeriodRecurrence.distanceTo(timeOfInterest.getTimeInMillis()));
  }

  @Test
  public void distanceTo_MUST_return_180000_WHEN_some_future_occurrence_happens_3_minutes_before_the_given_time() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_3_DAYS, 2000, 3, 1, 10, 10);
    Calendar timeOfInterest = toUtcCalendar(2000, 3, 13, 10, 13, 0);
    assertEquals(180000, dailyPeriodRecurrence.distanceTo(timeOfInterest.getTimeInMillis()));
  }

  @Test
  public void distanceTo_MUST_return_180000_WHEN_some_past_occurrence_happens_3_minutes_before_the_given_time() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_3_DAYS, 2000, 3, 13, 10, 10);
    Calendar timeOfInterest = toUtcCalendar(2000, 3, 10, 10, 13, 0);
    assertEquals(180000, dailyPeriodRecurrence.distanceTo(timeOfInterest.getTimeInMillis()));
  }

  @Test
  public void distanceTo_MUST_return_180000_WHEN_some_far_future_occurrence_happens_3_minutes_before_the_given_time() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_3_DAYS, 2000, 3, 1, 10, 10);
    Calendar timeOfInterest = toUtcCalendar(2014, 3, 12, 10, 13, 0);
    assertEquals(180000, dailyPeriodRecurrence.distanceTo(timeOfInterest.getTimeInMillis()));
  }

  @Test
  public void distanceTo_MUST_return_180000_WHEN_some_far_past_occurrence_happens_3_minutes_before_the_given_time() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_3_DAYS, 2000, 3, 1, 10, 10);
    Calendar timeOfInterest = toUtcCalendar(1985, 3, 2, 10, 13, 0);
    assertEquals(180000, dailyPeriodRecurrence.distanceTo(timeOfInterest.getTimeInMillis()));
  }

  @Test
  public void distanceTo_MUST_return_5h_30min() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_7_DAYS, 2014, 8, 29, 16, 10);
    long timeOfInterest = toUtcCalendar(2014, 7, 4, 21, 40, 0).getTimeInMillis();
    long distanceOf5h30min = 5 * HOUR_IN_MILLIS + 30 * MINUTE_IN_MILLIS;
    long calculatedDistance = dailyPeriodRecurrence.distanceTo(timeOfInterest);
    assertEquals(distanceOf5h30min, calculatedDistance);
  }

  @Test
  public void distanceTo_MUST_return_negative_5h_30min() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_7_DAYS, 2014, 7, 4, 21, 40);
    long timeOfInterest = toUtcCalendar(2014, 8, 29, 16, 10, 0).getTimeInMillis();
    long negativeDistance5h30min = -(5 * HOUR_IN_MILLIS + 30 * MINUTE_IN_MILLIS);
    long calculatedDistance = dailyPeriodRecurrence.distanceTo(timeOfInterest);
    assertEquals(negativeDistance5h30min, calculatedDistance);
  }

  @Test
  public void distanceTo_MUST_return_3days_11h_29mins() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_7_DAYS, 2014, 8, 0, 11, 30);
    long timeOfInterest = toUtcCalendar(2014, 8, 17, 22, 59, 0).getTimeInMillis();
    long negativeDistance5h30min = 3 * DAY_IN_MILLIS + 11 * HOUR_IN_MILLIS + 29 * MINUTE_IN_MILLIS;
    long calculatedDistance = dailyPeriodRecurrence.distanceTo(timeOfInterest);
    assertEquals(negativeDistance5h30min, calculatedDistance);
  }

  @Test
  public void distanceTo_MUST_return_negative_3days_11h_29mins() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_7_DAYS, 2014, 8, 0, 11, 30);
    long timeOfInterest = toUtcCalendar(2014, 8, 18, 0, 1, 0).getTimeInMillis();
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
    List<Long> foundOccurrences = createDailyPeriodRecurrence().getOccurrencesBetween(123, 123);
    assertThat(foundOccurrences, is(empty()));
  }

  private static DailyPeriodRecurrence createDailyPeriodRecurrence() {return new DailyPeriodRecurrence(1, 2, 3, 4, 5, 6);}
}