package si.urbas.chrony.recurrence.test.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matchers;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.Arrays;

public class AllOfRecurrenceMatcher extends RecurrenceMatcher {
  private final RecurrenceMatcher[] recurrenceMatchers;

  public AllOfRecurrenceMatcher(RecurrenceMatcher... recurrenceMatchers) {
    this.recurrenceMatchers = recurrenceMatchers;
  }

  public RecurrenceMatcher withPeriodOf(int periodInDays) {
    return and(new DailyPeriodRecurrenceMatcher(periodInDays));
  }

  public RecurrenceOccurringCloseToMatcherBuilder within(long distanceInMillis) {
    return new RecurrenceOccurringCloseToMatcherBuilder(this, distanceInMillis);
  }

  @Override
  protected boolean matches(Recurrence recurrence) {
    return Matchers.allOf(recurrenceMatchers).matches(recurrence);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("a recurrence");
    String isolatedDescription = isolatedDescription();
    if (!isolatedDescription.equals("")) {
      description.appendText(" ");
      description.appendText(isolatedDescription);
    }
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
    }
    return description.toString();
  }

  public AllOfRecurrenceMatcher and(RecurrenceMatcher recurrenceMatcher) {
    RecurrenceMatcher[] recurrenceMatchers = Arrays.copyOf(this.recurrenceMatchers, this.recurrenceMatchers.length + 1);
    recurrenceMatchers[this.recurrenceMatchers.length] = recurrenceMatcher;
    return new AllOfRecurrenceMatcher(recurrenceMatchers);
  }
}
