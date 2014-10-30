package si.urbas.chrony.recurrence.test.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import si.urbas.chrony.recurrence.Recurrence;

public class RecurrenceMatcher extends BaseMatcher<Recurrence> {

  @Override
  public boolean matches(Object item) {
    return item instanceof Recurrence && matches((Recurrence) item);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText(isolatedDescription());
  }

  protected String isolatedDescription() {
    return "recurrence";
  }

  protected boolean matches(Recurrence recurrence) {
    return true;
  }
}
