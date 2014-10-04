package si.urbas.chrony.descriptions;

import org.junit.Test;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrences;
import si.urbas.chrony.recurrence.RecurrencesList;

import static org.junit.Assert.assertEquals;
import static si.urbas.chrony.descriptions.RecurrenceDescriptions.DESCRIPTION_OF_EMPTY_RECURRENCE_ANALYSIS;
import static si.urbas.chrony.descriptions.RecurrenceDescriptions.toShortDescription;

public class RecurrenceDescriptionsTest {

  private static final int FIVE_O_CLOCK = 17;
  private static final int QUARTER_PAST_HOUR = 25;
  private static final int TWO_DAYS = 2;
  private static final int ONE_DAY = 1;
  private static final int SIXTEEN_O_CLOCK = 16;
  private static final int FIVE_PAST_HOUR = 5;

  @Test
  public void toShortDescriptionOf_MUST_say_that_no_recurrence_was_discovered_WHEN_the_recurrence_analysis_is_empty() {
    String actualDescription = toShortDescription(RecurrencesList.emptyRecurrences);
    assertEquals(DESCRIPTION_OF_EMPTY_RECURRENCE_ANALYSIS, actualDescription);
  }

  @Test
  public void toShortDescriptionOf_MUST_describe_daily_recurrences() {
    String actualDescription = toShortDescription(dayRecurrenceEvery(ONE_DAY, FIVE_O_CLOCK, QUARTER_PAST_HOUR));
    assertEquals("every day at 17:25", actualDescription);
  }

  @Test
  public void toShortDescriptionOf_MUST_describe_bidaily_recurrences() {
    String actualDescription = toShortDescription(dayRecurrenceEvery(TWO_DAYS, SIXTEEN_O_CLOCK, FIVE_PAST_HOUR));
    assertEquals("every 2nd day at 16:05", actualDescription);
  }

  private Recurrences dayRecurrenceEvery(int periodInDays, int hourOfDay, int minutesPastHour) {
    return new RecurrencesList(new DailyPeriodRecurrence(periodInDays, 0, 0, 0, hourOfDay, minutesPastHour));
  }
}