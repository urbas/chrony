package si.urbas.chrony.util;

import org.junit.Test;

import static org.junit.Assert.*;
import static si.urbas.chrony.util.TimeUtils.describePeriod;

public class TimeUtilsTest {

  @Test
  public void describePeriod_MUST_show_all_time_fields_up_to_a_year() {
      assertEquals("10 years 9 months 3 weeks 3 days 18h 32min 17.238s", describePeriod(341346737238L));
  }

}