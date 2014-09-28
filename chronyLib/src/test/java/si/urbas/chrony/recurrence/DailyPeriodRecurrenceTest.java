package si.urbas.chrony.recurrence;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static si.urbas.chrony.util.TimeUtils.createUtcCalendar;
import static si.urbas.chrony.util.TimeUtils.toUtcCalendar;

public class DailyPeriodRecurrenceTest {

  private static final int PERIOD_1_DAY = 1;
  private static final int PERIOD_3_DAYS = 3;

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
  public void equals_MUST_return_true_for_two_instances_created_with_same_parameters() {
    assertEquals(new DailyPeriodRecurrence(1, 2, 3, 4, 5, 6), new DailyPeriodRecurrence(1, 2, 3, 4, 5, 6));
  }

  @Test
  public void equals_MUST_return_false_for_two_instances_created_with_different_parameters() {
    assertNotEquals(new DailyPeriodRecurrence(1, 2, 3, 4, 0, 6), new DailyPeriodRecurrence(1, 2, 3, 4, 5, 6));
  }
}