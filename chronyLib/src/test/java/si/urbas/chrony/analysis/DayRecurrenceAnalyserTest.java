package si.urbas.chrony.analysis;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import si.urbas.chrony.Event;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.util.TimeUtils.*;

@SuppressWarnings("unchecked")
public class DayRecurrenceAnalyserTest extends RecurrenceAnalyserTest {

  private static final String EVENT_NAME = "event name";
  private static final int TWO_DAYS = 2;
  private final EventSample eventSampleAtTime0 = new EventSample(EVENT_NAME, TIME_0, null);
  private final EventSample eventSampleAtTime1d = new EventSample(EVENT_NAME, DAY_IN_MILLIS, null);
  private final EventSample eventSampleAtTime2d = new EventSample(EVENT_NAME, 2 * DAY_IN_MILLIS, null);
  private final EventSample eventSampleAtTime1w = new EventSample(EVENT_NAME, WEEK_IN_MILLIS, null);
  private final EventSample eventSampleAtTime9d = new EventSample(EVENT_NAME, 9 * DAY_IN_MILLIS, null);
  private final Matcher<Recurrence> isDailyPattern = isDailyPattern(1);
  private final Matcher<Recurrence> isWeeklyPattern = isDailyPattern(7);
  private Event event = new Event(EVENT_NAME, Event.NO_DATA_TYPE);

  @Override
  protected DayRecurrenceAnalyser createRecurrenceAnalyser(List<EventSample> eventSamples) {
    return new DayRecurrenceAnalyser(eventSamples);
  }

  @Test
  public void foundPatterns_MUST_return_an_empty_list_WHEN_given_no_event_samples() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(Collections.<EventSample>emptyList());
    assertThat(recurrenceAnalyser.foundPatterns(), is(empty()));
  }

  @Test
  public void constructor_MUST_return_an_empty_list_WHEN_the_entire_time_span_of_the_event_is_0() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime9d, eventSampleAtTime9d));
    assertThat(recurrenceAnalyser.foundPatterns(), is(empty()));
  }

  @Test
  public void foundPatterns_MUST_return_an_empty_list_WHEN_given_a_single_event_sample() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime0));
    assertThat(recurrenceAnalyser.foundPatterns(), is(empty()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructor_MUST_throw_an_exception_WHEN_the_list_of_event_samples_is_not_sorted() {
    createRecurrenceAnalyser(asList(eventSampleAtTime1d, eventSampleAtTime0));
  }

  @Test
  public void foundPatterns_MUST_return_a_daily_pattern_WHEN_given_two_events_a_day_apart() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime0, eventSampleAtTime1d));
    assertThat(recurrenceAnalyser.foundPatterns(), contains(isDailyPattern));
  }

  @Test
  public void foundPatterns_MUST_return_a_weekly_pattern_WHEN_given_two_events_a_week_apart() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime0, eventSampleAtTime1w));
    assertThat(recurrenceAnalyser.foundPatterns(), contains(isWeeklyPattern));
  }

  @Test
  public void foundPatterns_MUST_return_a_bidaily_pattern_WHEN_given_two_events_that_are_two_days_apart() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime0, eventSampleAtTime2d));
    assertThat(recurrenceAnalyser.foundPatterns(), contains(isDailyPattern(TWO_DAYS)));
  }

  @Test
  public void foundPatterns_MUST_return_two_weekly_patterns_WHEN_given_four_events() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime0, eventSampleAtTime2d, eventSampleAtTime1w, eventSampleAtTime9d));
    assertThat(recurrenceAnalyser.foundPatterns(), contains(isWeeklyPattern, isWeeklyPattern));
  }

  @Test
  public void foundPatterns_MUST_return_two_weekly_patterns_WHEN_they_pairwise_happen_on_same_weekday_but_at_different_time() {
    List<EventSample> eventSamples = asList(eventSampleAtTime(1, 9), eventSampleAtTime(1, 20), eventSampleAtTime(8, 9), eventSampleAtTime(8, 20));
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(eventSamples);
    assertThat(recurrenceAnalyser.foundPatterns(), contains(isWeeklyPattern, isWeeklyPattern));
  }

  private EventSample eventSampleAtTime(int day, int hour) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(0);
    calendar.add(Calendar.HOUR, day * 24 + hour);
    return new EventSample(EVENT_NAME, calendar.getTimeInMillis(), null);
  }

  private Matcher<Recurrence> isDailyPattern(final int daysApart) {
    return new DailyRecurrencePatternMatcher(daysApart);
  }

  private static class DailyRecurrencePatternMatcher extends BaseMatcher<Recurrence> {

    private final int daysApart;

    public DailyRecurrencePatternMatcher(int daysApart) {this.daysApart = daysApart;}

    @Override
    public boolean matches(Object item) {
      return item instanceof DailyPeriodRecurrence && matches((DailyPeriodRecurrence) item);
    }

    public boolean matches(DailyPeriodRecurrence recurrencePattern) {
      int period = recurrencePattern.getPeriodInDays();
      return period == daysApart;
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("daily recurrence with a period of " + daysApart + " day(s)");
    }
  }
}