package si.urbas.chrony.analysis;

import org.hamcrest.Matcher;
import org.junit.Test;
import si.urbas.chrony.EventSample;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.analysis.RecurrenceAnalysis.extractRecurrencePattern;
import static si.urbas.chrony.util.TimeUtils.*;

public class RecurrenceAnalysisTest {


  private static final String EVENT_NAME = "event name";
  private final EventSample eventSampleAtTime0 = new EventSample(EVENT_NAME, TIME_0, null);
  private final EventSample eventSampleAtTime1d = new EventSample(EVENT_NAME, MILLIS_1_DAY, null);
  private final EventSample eventSampleAtTime1w = new EventSample(EVENT_NAME, MILLIS_1_WEEK, null);
  private final Matcher<Object> isUnknownRecurrencePattern = is(instanceOf(UnknownRecurrencePattern.class));

  @Test
  public void extractRecurrencePattern_MUST_return_an_unknown_recurrence_pattern_WHEN_given_no_event_samples() {
    RecurrencePattern discoveredRecurrencePattern = extractRecurrencePattern(Collections.<EventSample>emptyList());
    assertThat(discoveredRecurrencePattern, isUnknownRecurrencePattern);
  }

  @Test
  public void extractRecurrencePattern_MUST_return_an_unknown_recurrence_pattern_WHEN_given_only_one_event_sample() {
    RecurrencePattern discoveredRecurrencePattern = extractRecurrencePattern(asList(eventSampleAtTime0));
    assertThat(discoveredRecurrencePattern, isUnknownRecurrencePattern);
  }

  @Test
  public void extractRecurrencePattern_MUST_return_a_weekly_recurrence_pattern_WHEN_two_samples_occur_exactly_one_week_apart() {
    RecurrencePattern discoveredRecurrencePattern = extractRecurrencePattern(asList(eventSampleAtTime0, eventSampleAtTime1w));
    assertThat(discoveredRecurrencePattern, is(instanceOf(WeeklyRecurrencePattern.class)));
  }

  @Test
  public void extractRecurrencePattern_MUST_return_a_daily_pattern_WHEN_two_samples_occur_exactly_one_day_apart() {
    RecurrencePattern discoveredRecurrencePattern = extractRecurrencePattern(asList(eventSampleAtTime0, eventSampleAtTime1d));
    assertThat(discoveredRecurrencePattern, is(instanceOf(DailyRecurrencePattern.class)));
  }

}