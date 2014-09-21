package si.urbas.chrony.descriptions;

import org.junit.Test;
import si.urbas.chrony.recurrence.ConstantPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static si.urbas.chrony.descriptions.RecurrenceDescriptions.DESCRIPTION_OF_EMPTY_RECURRENCE_ANALYSIS;
import static si.urbas.chrony.descriptions.RecurrenceDescriptions.getShortDescriptionOf;

public class RecurrenceDescriptionsTest {

  private static final int FIVE_O_CLOCK = 17;
  private static final int QUARTER_PAST_HOUR = 25;
  private static final int TWO_DAYS = 2;
  private static final int ONE_DAY = 1;
  private static final int FOUR_O_CLOCK = 4;
  private static final int FORTY_PAST_HOUR = 40;

  @Test
  public void getShortDescriptionOf_MUST_say_that_no_recurrence_was_discovered_WHEN_the_recurrence_analysis_is_empty() {
    String actualDescription = getShortDescriptionOf(Collections.<Recurrence>emptyList());
    assertEquals(DESCRIPTION_OF_EMPTY_RECURRENCE_ANALYSIS, actualDescription);
  }

  @Test
  public void getShortDescriptionOf_MUST_describe_daily_recurrences() {
    String actualDescription = getShortDescriptionOf(dayRecurrenceEvery(ONE_DAY, FIVE_O_CLOCK, QUARTER_PAST_HOUR));
    assertEquals("at 17:25 every day", actualDescription);
  }

  @Test
  public void getShortDescriptionOf_MUST_describe_bidaily_recurrences() {
    String actualDescription = getShortDescriptionOf(dayRecurrenceEvery(TWO_DAYS, FOUR_O_CLOCK, FORTY_PAST_HOUR));
    assertEquals("at 16:40 every second day", actualDescription);
  }

  private List<ConstantPeriodRecurrence> dayRecurrenceEvery(int periodInDays, int hourOfDay, int minutesPastHour) {
    return Arrays.asList(new ConstantPeriodRecurrence(periodInDays, hourOfDay, minutesPastHour));
  }
}