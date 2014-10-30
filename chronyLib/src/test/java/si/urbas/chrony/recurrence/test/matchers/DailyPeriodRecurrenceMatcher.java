package si.urbas.chrony.recurrence.test.matchers;

import org.hamcrest.Description;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;

public class DailyPeriodRecurrenceMatcher extends RecurrenceMatcher {

  private final int periodInDays;

  public DailyPeriodRecurrenceMatcher(int periodInDays) {this.periodInDays = periodInDays;}

  @Override
  protected boolean matches(Recurrence recurrence) {
    return recurrence instanceof DailyPeriodRecurrence &&
           matches((DailyPeriodRecurrence) recurrence);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("a recurrence ");
    description.appendText(isolatedDescription());
  }

  @Override
  protected String isolatedDescription() {
    return "having a period of " + periodInDays + " day(s)";
  }

  private boolean matches(DailyPeriodRecurrence recurrence) {
    int period = recurrence.getPeriodInDays();
    return period == periodInDays;
  }
}
