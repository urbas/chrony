package si.urbas.chrony.recurrence.test.matchers;

public class RecurrenceMatchers {

  public static RecurrenceOccurringCloseToMatcherBuilder recurrenceWithin(long maxDistanceToOccurrence) {
    return new RecurrenceOccurringCloseToMatcherBuilder(maxDistanceToOccurrence);
  }

  public static DailyPeriodRecurrenceMatcher recurrenceWithPeriodOf(final int daysApart) {
    return new DailyPeriodRecurrenceMatcher(daysApart);
  }

}
