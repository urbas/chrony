package si.urbas.chrony.util;

import org.junit.Test;

import static org.junit.Assert.*;
import static si.urbas.chrony.util.TimeUtils.formatPeriod;

public class TimeUtilsTest {

  @Test
  public void formatPeriod_MUST_show_all_time_fields_up_to_a_year() {
      assertEquals("10 years, 9 months, 3 weeks, 3 days, 18 hours, 32 minutes, 17 seconds and 238 milliseconds", formatPeriod(341346737238L));
  }

}