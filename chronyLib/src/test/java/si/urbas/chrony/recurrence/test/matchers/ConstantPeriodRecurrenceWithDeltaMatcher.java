package si.urbas.chrony.recurrence.test.matchers;

import org.hamcrest.Description;
import si.urbas.chrony.recurrence.ConstantPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;

import static si.urbas.chrony.util.MathUtils.isWithin;
import static si.urbas.chrony.util.TimeUtils.describePeriod;

public class ConstantPeriodRecurrenceWithDeltaMatcher extends RecurrenceMatcher {

  private final long expectedPeriodInMillis;
  private final long expectedDelta;

  public ConstantPeriodRecurrenceWithDeltaMatcher(long expectedPeriodInMillis, long expectedDelta) {
    this.expectedPeriodInMillis = expectedPeriodInMillis;
    this.expectedDelta = expectedDelta;
  }

  @Override
  protected boolean matches(Recurrence recurrence) {
    return recurrence instanceof ConstantPeriodRecurrence &&
           matches((ConstantPeriodRecurrence) recurrence);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("a recurrence ");
    description.appendText(isolatedDescription());
  }

  @Override
  protected String isolatedDescription() {
    return "having a period of " + describePeriod(expectedPeriodInMillis) + " +- " + describePeriod(expectedDelta);
  }

  private boolean matches(ConstantPeriodRecurrence recurrence) {
    long actualPeriodInMillis = recurrence.getPeriodInMillis();
    return isWithin(actualPeriodInMillis, expectedPeriodInMillis, expectedDelta);
  }
}
