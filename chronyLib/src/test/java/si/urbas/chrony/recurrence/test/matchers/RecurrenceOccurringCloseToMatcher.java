package si.urbas.chrony.recurrence.test.matchers;

import org.hamcrest.Description;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.util.TimeUtils;

public class RecurrenceOccurringCloseToMatcher extends RecurrenceMatcher {

  private final long targetOccurrenceTimeInMillis;
  private final long maxDistanceToOccurrence;

  public RecurrenceOccurringCloseToMatcher(long targetOccurrenceTimeInMillis, long maxDistanceToOccurrence) {
    this.targetOccurrenceTimeInMillis = targetOccurrenceTimeInMillis;
    this.maxDistanceToOccurrence = maxDistanceToOccurrence;
  }

  @Override
  public void describeTo(Description description) {
    super.describeTo(description);
    description.appendText(" with ");
    description.appendText(isolatedDescription());
  }

  @Override
  protected String isolatedDescription() {
    return "an occurrence " + describeDistance() + " away from " + TimeUtils.toSimpleString(targetOccurrenceTimeInMillis);
  }

  private String describeDistance() {
    return ((double) maxDistanceToOccurrence / (double) TimeUtils.SECOND_IN_MILLIS) + " seconds";
  }

  @Override
  protected boolean matches(Recurrence recurrence) {
    return Math.abs(recurrence.distanceTo(targetOccurrenceTimeInMillis)) <= maxDistanceToOccurrence;
  }
}