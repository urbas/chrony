package si.urbas.chrony.recurrence.test.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matchers;
import si.urbas.chrony.recurrence.Recurrence;

public class AllOfRecurrenceMatcher extends RecurrenceMatcher {
  private final RecurrenceMatcher[] recurrenceMatchers;

  public AllOfRecurrenceMatcher(RecurrenceMatcher... recurrenceMatchers) {
    this.recurrenceMatchers = recurrenceMatchers;
  }

  @Override
  protected boolean matches(Recurrence recurrence) {
    return Matchers.allOf(recurrenceMatchers).matches(recurrence);
  }

  @Override
  public void describeTo(Description description) {
    super.describeTo(description);
    if (recurrenceMatchers.length > 0) {
      description.appendText(" with ");
      description.appendText(recurrenceMatchers[0].isolatedDescription());
      for (int i = 1; i < recurrenceMatchers.length; i++) {
        description.appendText(" and ");
        description.appendText(recurrenceMatchers[i].isolatedDescription());
      }
    }
  }
}
