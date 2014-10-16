package si.urbas.chrony.recurrence.test.matchers;

import org.hamcrest.BaseMatcher;
import si.urbas.chrony.recurrence.Recurrence;

public abstract class RecurrenceMatcher extends BaseMatcher<Recurrence> {

  @Override
  public boolean matches(Object item) {
    return item instanceof Recurrence && matches((Recurrence) item);
  }

  public RecurrenceMatcher withPeriodOf(int periodInDays) {
    return new AllOfRecurrenceMatcher(this, new DailyPeriodRecurrenceMatcher(periodInDays));
  }

  protected abstract String isolatedDescription();

  protected abstract boolean matches(Recurrence recurrence);
}
