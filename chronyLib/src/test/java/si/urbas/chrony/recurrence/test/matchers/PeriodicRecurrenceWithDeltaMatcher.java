package si.urbas.chrony.recurrence.test.matchers;

import org.hamcrest.Description;
import si.urbas.chrony.recurrence.PeriodicRecurrence;
import si.urbas.chrony.recurrence.Recurrence;

import static si.urbas.chrony.util.MathUtils.isWithin;
import static si.urbas.chrony.util.TimeUtils.formatPeriod;

public class PeriodicRecurrenceWithDeltaMatcher extends RecurrenceMatcher {

  private final long expectedPeriodInMillis;
  private final long expectedDelta;

  public PeriodicRecurrenceWithDeltaMatcher(long expectedPeriodInMillis, long expectedDelta) {
    this.expectedPeriodInMillis = expectedPeriodInMillis;
    this.expectedDelta = expectedDelta;
  }

  @Override
  protected boolean matches(Recurrence recurrence) {
    return recurrence instanceof PeriodicRecurrence &&
           matches((PeriodicRecurrence) recurrence);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("a recurrence ");
    description.appendText(isolatedDescription());
  }

  @Override
  protected String isolatedDescription() {
    String periodDescription = "having a period of " + formatPeriod(expectedPeriodInMillis);
    if (expectedDelta == 0) {
      return periodDescription;
    } else {
      return periodDescription + " +- " + formatPeriod(expectedDelta);
    }
  }

  private boolean matches(PeriodicRecurrence recurrence) {
    long actualPeriodInMillis = recurrence.getPeriodInMillis();
    return isWithin(actualPeriodInMillis, expectedPeriodInMillis, expectedDelta);
  }
}
