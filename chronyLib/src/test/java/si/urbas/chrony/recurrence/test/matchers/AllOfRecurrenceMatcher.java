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
    description.appendText("recurrence ");
    description.appendText(isolatedDescription());
  }

  @Override
  protected String isolatedDescription() {
      StringBuilder description = new StringBuilder();
    if (recurrenceMatchers.length > 0) {
      description.append(recurrenceMatchers[0].isolatedDescription());
      for (int i = 1; i < recurrenceMatchers.length; i++) {
        description.append(" and ");
        description.append(recurrenceMatchers[i].isolatedDescription());
      }
    } else {
      description.append("having no other constraints");
    }
    return description.toString();
  }
}
