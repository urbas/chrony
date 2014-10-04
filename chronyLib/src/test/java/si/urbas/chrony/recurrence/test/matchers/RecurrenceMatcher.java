package si.urbas.chrony.recurrence.test.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import si.urbas.chrony.recurrence.Recurrence;

public abstract class RecurrenceMatcher extends BaseMatcher<Recurrence> {

  @Override
  public boolean matches(Object item) {
    return item instanceof Recurrence && matches((Recurrence) item);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText(isolatedDescription());
  }

  public RecurrenceMatcher withPeriodOf(int periodInDays) {
    return new AllOfRecurrenceMatcher(this, new DailyPeriodRecurrenceMatcher(periodInDays));
  }

  protected String isolatedDescription() {
    return "recurrence";
  }

  protected abstract boolean matches(Recurrence recurrence);
}
