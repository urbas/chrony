package si.urbas.chrony.recurrence.test.matchers;

import org.hamcrest.Description;
import si.urbas.chrony.recurrence.Recurrence;

import static si.urbas.chrony.util.TimeUtils.formatPeriod;
import static si.urbas.chrony.util.TimeUtils.formatDate;

public class RecurrenceOccurringCloseToMatcher extends RecurrenceMatcher {

  private final long targetOccurrenceTimeInMillis;
  private final long maxDistanceToOccurrence;

  public RecurrenceOccurringCloseToMatcher(long targetOccurrenceTimeInMillis, long maxDistanceToOccurrence) {
    this.targetOccurrenceTimeInMillis = targetOccurrenceTimeInMillis;
    this.maxDistanceToOccurrence = maxDistanceToOccurrence;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("a recurrence ");
    description.appendText(isolatedDescription());
  }

  @Override
  protected String isolatedDescription() {
    return "happening within " + formatPeriod(maxDistanceToOccurrence) + " of " + formatDate(targetOccurrenceTimeInMillis);
  }

  @Override
  protected boolean matches(Recurrence recurrence) {
    return Math.abs(recurrence.distanceTo(targetOccurrenceTimeInMillis)) <= maxDistanceToOccurrence;
  }
}
