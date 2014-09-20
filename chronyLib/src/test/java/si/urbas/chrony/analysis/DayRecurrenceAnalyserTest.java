package si.urbas.chrony.analysis;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import si.urbas.chrony.Event;
import si.urbas.chrony.EventSample;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.util.TimeUtils.*;

@SuppressWarnings("unchecked")
public class DayRecurrenceAnalyserTest {

  private static final String EVENT_NAME = "event name";
  private static final int TWO_DAYS = 2;
  private final EventSample eventSampleAtTime0 = new EventSample(EVENT_NAME, TIME_0, null);
  private final EventSample eventSampleAtTime1d = new EventSample(EVENT_NAME, DAY_IN_MILLIS, null);
  private final EventSample eventSampleAtTime2d = new EventSample(EVENT_NAME, 2 * DAY_IN_MILLIS, null);
  private final EventSample eventSampleAtTime1w = new EventSample(EVENT_NAME, WEEK_IN_MILLIS, null);
  private final EventSample eventSampleAtTime9d = new EventSample(EVENT_NAME, 9 * DAY_IN_MILLIS, null);
  private final Matcher<DailyRecurrencePattern> isDailyPattern = isDailyPattern(1);
  private final Matcher<DailyRecurrencePattern> isWeeklyPattern = isDailyPattern(7);
  private Event event = new Event(EVENT_NAME, Event.NO_DATA_TYPE);

  @Test
  public void foundPatterns_MUST_return_an_empty_list_WHEN_given_no_event_samples() {
    DayRecurrenceAnalyser dayRecurrenceAnalyser = new DayRecurrenceAnalyser(event, Collections.<EventSample>emptyList());
    assertThat(dayRecurrenceAnalyser.foundPatterns(), is(empty()));
  }

  @Test
  public void constructor_MUST_return_an_empty_list_WHEN_the_entire_time_span_of_the_event_is_0() {
    DayRecurrenceAnalyser dayRecurrenceAnalyser = new DayRecurrenceAnalyser(event, asList(eventSampleAtTime9d, eventSampleAtTime9d));
    assertThat(dayRecurrenceAnalyser.foundPatterns(), is(empty()));
  }

  @Test
  public void foundPatterns_MUST_return_an_empty_list_WHEN_given_a_single_event_sample() {
    DayRecurrenceAnalyser dayRecurrenceAnalyser = new DayRecurrenceAnalyser(event, asList(eventSampleAtTime0));
    assertThat(dayRecurrenceAnalyser.foundPatterns(), is(empty()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructor_MUST_throw_an_exception_WHEN_the_list_of_event_samples_is_not_sorted() {
    new DayRecurrenceAnalyser(event, asList(eventSampleAtTime1d, eventSampleAtTime0));
  }

  @Test
  public void foundPatterns_MUST_return_a_daily_pattern_WHEN_given_two_events_a_day_apart() {
    DayRecurrenceAnalyser dayRecurrenceAnalyser = new DayRecurrenceAnalyser(event, asList(eventSampleAtTime0, eventSampleAtTime1d));
    assertThat(dayRecurrenceAnalyser.foundPatterns(), contains(isDailyPattern));
  }

  @Test
  public void foundPatterns_MUST_return_a_weekly_pattern_WHEN_given_two_events_a_week_apart() {
    DayRecurrenceAnalyser dayRecurrenceAnalyser = new DayRecurrenceAnalyser(event, asList(eventSampleAtTime0, eventSampleAtTime1w));
    assertThat(dayRecurrenceAnalyser.foundPatterns(), contains(isWeeklyPattern));
  }

  @Test
  public void foundPatterns_MUST_return_a_bidaily_pattern_WHEN_given_two_events_that_are_two_days_apart() {
    DayRecurrenceAnalyser dayRecurrenceAnalyser = new DayRecurrenceAnalyser(event, asList(eventSampleAtTime0, eventSampleAtTime2d));
    assertThat(dayRecurrenceAnalyser.foundPatterns(), contains(isDailyPattern(TWO_DAYS)));
  }

  @Test
  public void foundPatterns_MUST_return_two_weekly_patterns_WHEN_given_four_events() {
    DayRecurrenceAnalyser dayRecurrenceAnalyser = new DayRecurrenceAnalyser(event, asList(eventSampleAtTime0, eventSampleAtTime2d, eventSampleAtTime1w, eventSampleAtTime9d));
    assertThat(dayRecurrenceAnalyser.foundPatterns(), contains(isWeeklyPattern, isWeeklyPattern));
  }

  @Test
  public void foundPatterns_MUST_return_two_weekly_patterns_WHEN_they_pairwise_happen_on_same_weekday_but_at_different_time() {
    List<EventSample> eventSamples = asList(eventSampleAtTime(1, 9), eventSampleAtTime(1, 20), eventSampleAtTime(8, 9), eventSampleAtTime(8, 20));
    DayRecurrenceAnalyser dayRecurrenceAnalyser = new DayRecurrenceAnalyser(event, eventSamples);
    assertThat(dayRecurrenceAnalyser.foundPatterns(), contains(isWeeklyPattern, isWeeklyPattern));
  }

  private EventSample eventSampleAtTime(int day, int hour) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(0);
    calendar.add(Calendar.HOUR, day * 24 + hour);
    return new EventSample(EVENT_NAME, calendar.getTimeInMillis(), null);
  }

  private Matcher<DailyRecurrencePattern> isDailyPattern(final int daysApart) {
    return new DailyRecurrencePatternMatcher(daysApart);
  }

  private static class DailyRecurrencePatternMatcher extends BaseMatcher<DailyRecurrencePattern> {

    private final int daysApart;

    public DailyRecurrencePatternMatcher(int daysApart) {this.daysApart = daysApart;}

    @Override
    public boolean matches(Object item) {
      return item instanceof DailyRecurrencePattern && matches((DailyRecurrencePattern) item);
    }

    public boolean matches(DailyRecurrencePattern recurrencePattern) {
      int period = recurrencePattern.getPeriod();
      return period == daysApart;
    }

    @Override
    public void describeTo(Description description) {
      description.appendText("daily recurrence with a period of " + daysApart + " day(s)");
    }
  }
}