package si.urbas.chrony.analysis;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import si.urbas.chrony.EventSample;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.util.TimeUtils.*;

public class DayRecurrenceAnalyserTest {


  private static final String EVENT_NAME = "event name";
  private static final int TWO_DAYS = 2;
  private final EventSample eventSampleAtTime0 = new EventSample(EVENT_NAME, TIME_0, null);
  private final EventSample eventSampleAtTime1d = new EventSample(EVENT_NAME, MILLIS_1_DAY, null);
  private final EventSample eventSampleAtTime2d = new EventSample(EVENT_NAME, 2 * MILLIS_1_DAY, null);
  private final EventSample eventSampleAtTime1w = new EventSample(EVENT_NAME, MILLIS_1_WEEK, null);
  private final EventSample eventSampleAtTime9d = new EventSample(EVENT_NAME, 9 * MILLIS_1_DAY, null);
  private final Matcher<DailyRecurrencePattern> isDailyPattern = isDailyPattern(1);
  private final Matcher<DailyRecurrencePattern> isWeeklyPattern = isDailyPattern(7);

  @Test
  public void foundPatterns_MUST_return_an_empty_list_WHEN_given_no_event_samples() {
    DayRecurrenceAnalyser dayRecurrenceAnalyser = new DayRecurrenceAnalyser(Collections.<EventSample>emptyList());
    assertThat(dayRecurrenceAnalyser.foundPatterns(), is(empty()));
  }

  @Test
  public void foundPatterns_MUST_return_an_empty_list_WHEN_given_a_single_event_sample() {
    DayRecurrenceAnalyser dayRecurrenceAnalyser = new DayRecurrenceAnalyser(asList(eventSampleAtTime0));
    assertThat(dayRecurrenceAnalyser.foundPatterns(), is(empty()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructor_MUST_throw_an_exception_WHEN_the_list_of_event_samples_is_not_sorted() {
    new DayRecurrenceAnalyser(asList(eventSampleAtTime1d, eventSampleAtTime0));
  }

  @Test
  public void foundPatterns_MUST_return_a_daily_pattern_WHEN_given_two_events_a_day_apart() {
    DayRecurrenceAnalyser dayRecurrenceAnalyser = new DayRecurrenceAnalyser(asList(eventSampleAtTime0, eventSampleAtTime1d));
    assertThat(dayRecurrenceAnalyser.foundPatterns(), contains(isDailyPattern));
  }

  @Test
  public void foundPatterns_MUST_return_a_weekly_pattern_WHEN_given_two_events_a_week_apart() {
    DayRecurrenceAnalyser dayRecurrenceAnalyser = new DayRecurrenceAnalyser(asList(eventSampleAtTime0, eventSampleAtTime1w));
    assertThat(dayRecurrenceAnalyser.foundPatterns(), contains(isWeeklyPattern));
  }

  @Test
  public void foundPatterns_MUST_return_a_bidaily_pattern_WHEN_given_two_events_that_are_two_days_apart() {
    DayRecurrenceAnalyser dayRecurrenceAnalyser = new DayRecurrenceAnalyser(asList(eventSampleAtTime0, eventSampleAtTime2d));
    assertThat(dayRecurrenceAnalyser.foundPatterns(), contains(isDailyPattern(TWO_DAYS)));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void foundPatterns_MUST_return_two_daily_patterns_WHEN_given_a_four_events() {
    DayRecurrenceAnalyser dayRecurrenceAnalyser = new DayRecurrenceAnalyser(asList(eventSampleAtTime0, eventSampleAtTime2d, eventSampleAtTime1w, eventSampleAtTime9d));
    assertThat(dayRecurrenceAnalyser.foundPatterns(), contains(isWeeklyPattern, isWeeklyPattern));
  }

  private Matcher<DailyRecurrencePattern> isDailyPattern(final int daysApart) {
    return new BaseMatcher<DailyRecurrencePattern>() {
      @Override
      public boolean matches(Object item) {
        return item instanceof DailyRecurrencePattern && matches((DailyRecurrencePattern)item);
      }

      public boolean matches(DailyRecurrencePattern recurrencePattern) {
        int period = recurrencePattern.getPeriod();
        return period == daysApart;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("daily recurrence with a period of " + daysApart + " day(s)");
      }
    };
  }

}