package si.urbas.chrony.recurrence;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;
import static si.urbas.chrony.util.TimeUtils.createUtcCalendar;

public class DailyPeriodRecurrenceTest {

  private static final int PERIOD_1_DAY = 1;

  @Test
  public void timeDifference_MUST_return_0_WHEN_the_recurrence_starts_exactly_on_the_given_time() {
    Calendar calendar = createUtcCalendar();
    new DailyPeriodRecurrence(PERIOD_1_DAY, calendar.getTimeInMillis());
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