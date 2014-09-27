package si.urbas.chrony.recurrence;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static si.urbas.chrony.util.TimeUtils.createUtcCalendar;
import static si.urbas.chrony.util.TimeUtils.toUtcCalendar;

public class DailyPeriodRecurrenceTest {

  private static final int PERIOD_1_DAY = 1;

  @Test
  public void differenceTo_MUST_return_0_WHEN_the_nearest_recurrence_happens_exactly_on_the_given_time() {
    Calendar calendar = createUtcCalendar();
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_1_DAY, calendar);
    assertEquals(0, dailyPeriodRecurrence.differenceTo(calendar.getTimeInMillis()));
  }

  @Test
  public void differenceTo_MUST_return_60000_WHEN_the_nearest_recurrence_happens_one_minute_before_the_given_time() {
    DailyPeriodRecurrence dailyPeriodRecurrence = new DailyPeriodRecurrence(PERIOD_1_DAY, 2000, 3, 14, 15, 32);
    Calendar timeOfInterest = toUtcCalendar(2000, 3, 14, 15, 31, 0);
    assertEquals(60000, dailyPeriodRecurrence.differenceTo(timeOfInterest.getTimeInMillis()));
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