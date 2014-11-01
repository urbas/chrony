package si.urbas.chrony.util;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static si.urbas.chrony.util.TimeUtils.*;

public class TimeUtilsTest {

  @Test
  public void withTimeOfDay_MUST_return_a_new_calendar_with_correct_hour_minute_second_and_millisecond_fields() {
    Calendar calendar = TimeUtils.toUtcCalendar(2014, 8, 17, 15, 29, 34, 784);
    assertEquals(
      calendar,
      withTimeOfDay(withDateOnly(calendar), timeOfDayInMillis(calendar))
    );
  }

}