package si.urbas.chrony.recurrence.test.matchers;

public class RecurrenceOccurringCloseToMatcherBuilder {

  private final long maxDistanceToOccurrence;

  public RecurrenceOccurringCloseToMatcherBuilder(long maxDistanceToOccurrence) {
    this.maxDistanceToOccurrence = maxDistanceToOccurrence;
  }

  public RecurrenceOccurringCloseToMatcher of(long firstOccurrenceTimeInMillis) {
    return new RecurrenceOccurringCloseToMatcher(firstOccurrenceTimeInMillis, maxDistanceToOccurrence);
  }
}
