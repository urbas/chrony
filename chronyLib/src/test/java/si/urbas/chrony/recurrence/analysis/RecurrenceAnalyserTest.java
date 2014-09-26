package si.urbas.chrony.recurrence.analysis;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.EventSamplesTestUtils.*;

public abstract class RecurrenceAnalyserTest {

  private static final int TWO_DAYS = 2;
  protected final EventSample eventSampleAtTime1d17h = eventSampleAtTime(DAY_1, HOUR_17);
  protected final EventSample eventSampleAtTime2d17h = eventSampleAtTime(DAY_2, HOUR_17);
  protected final EventSample eventSampleAtTime3d17h = eventSampleAtTime(DAY_3, HOUR_17);
  protected final EventSample eventSampleAtTime8d17h = eventSampleAtTime(DAY_8, HOUR_17);
  protected final EventSample eventSampleAtTime10d17h = eventSampleAtTime(DAY_10, HOUR_17);
  protected final Matcher<Recurrence> weeklyRecurrence = recurrenceWithPeriodOf(7);
  protected final Matcher<Recurrence> dailyRecurrence = recurrenceWithPeriodOf(1);

  @Test
  public void foundRecurrences_MUST_return_an_empty_list_WHEN_given_no_event_samples() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(emptyEventSamples());
    assertThat(recurrenceAnalyser.foundRecurrences(), is(empty()));
  }

  @Test
  public void foundRecurrences_MUST_return_an_empty_list_WHEN_the_entire_time_span_of_the_event_is_0() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime10d17h, eventSampleAtTime10d17h));
    assertThat(recurrenceAnalyser.foundRecurrences(), is(empty()));
  }

  @Test
  public void foundRecurrences_MUST_return_an_empty_list_WHEN_given_a_single_event_sample() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime1d17h));
    assertThat(recurrenceAnalyser.foundRecurrences(), is(empty()));
  }

  @Test
  public void foundRecurrences_MUST_return_a_daily_recurrence_WHEN_given_two_events_a_day_apart() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime1d17h, eventSampleAtTime2d17h));
    assertThat(recurrenceAnalyser.foundRecurrences(), contains(dailyRecurrence));
  }

  @Test
  public void foundRecurrences_MUST_return_a_weekly_recurrence_WHEN_given_two_events_a_week_apart() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime1d17h, eventSampleAtTime8d17h));
    assertThat(recurrenceAnalyser.foundRecurrences(), contains(weeklyRecurrence));
  }

  @Test
  public void foundRecurrences_MUST_return_a_recurrence_of_every_second_day_WHEN_given_two_events_that_are_two_days_apart() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime1d17h, eventSampleAtTime3d17h));
    assertThat(recurrenceAnalyser.foundRecurrences(), contains(recurrenceWithPeriodOf(TWO_DAYS)));
  }

  protected abstract RecurrenceAnalyser createRecurrenceAnalyser(List<EventSample> eventSamples);

  public static Matcher<Recurrence> recurrenceWithPeriodOf(final int daysApart) {
    return new RecurrenceWithDailyPeriodMatcher(daysApart);
  }

  private static class RecurrenceWithDailyPeriodMatcher extends BaseMatcher<Recurrence> {

    private final int daysApart;

    public RecurrenceWithDailyPeriodMatcher(int daysApart) {this.daysApart = daysApart;}

    @Override
    public boolean matches(Object item) {
      return item instanceof DailyPeriodRecurrence && matches((DailyPeriodRecurrence) item);
    }

    public boolean matches(DailyPeriodRecurrence recurrence) {
      int period = recurrence.getPeriodInDays();
      return period == daysApart;
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("daily recurrence with a period of " + daysApart + " day(s)");
    }
  }
}
