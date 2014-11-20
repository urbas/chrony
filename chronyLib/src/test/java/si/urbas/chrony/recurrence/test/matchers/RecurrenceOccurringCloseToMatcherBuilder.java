package si.urbas.chrony.recurrence.test.matchers;

import org.joda.time.DateTime;

public class RecurrenceOccurringCloseToMatcherBuilder {

  private final AllOfRecurrenceMatcher allOfRecurrenceMatcher;
  private final long maxDistanceToOccurrence;

  public RecurrenceOccurringCloseToMatcherBuilder(AllOfRecurrenceMatcher allOfRecurrenceMatcher, long maxDistanceToOccurrence) {
    this.allOfRecurrenceMatcher = allOfRecurrenceMatcher;
    this.maxDistanceToOccurrence = maxDistanceToOccurrence;
  }

  public AllOfRecurrenceMatcher of(long firstOccurrenceTimeInMillis) {
    return allOfRecurrenceMatcher.and(new RecurrenceOccurringCloseToMatcher(firstOccurrenceTimeInMillis, maxDistanceToOccurrence));
  }

  public AllOfRecurrenceMatcher of(DateTime timestamp) {
    return of(timestamp.getMillis());
  }
}
