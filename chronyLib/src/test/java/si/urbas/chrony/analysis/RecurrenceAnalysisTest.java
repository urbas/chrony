package si.urbas.chrony.analysis;

import org.hamcrest.Matcher;
import org.junit.Test;
import si.urbas.chrony.EventSample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.analysis.RecurrenceAnalysis.extractDailyRecurrencePattern;
import static si.urbas.chrony.analysis.RecurrenceAnalysis.extractRecurrencePattern;
import static si.urbas.chrony.util.TimeUtils.*;

public class RecurrenceAnalysisTest {


  private static final String EVENT_NAME = "event name";
  private final EventSample eventSampleAtTime0 = new EventSample(EVENT_NAME, TIME_0, null);
  private final EventSample eventSampleAtTime1d = new EventSample(EVENT_NAME, MILLIS_1_DAY, null);
  private final EventSample eventSampleAtTime1w = new EventSample(EVENT_NAME, MILLIS_1_WEEK, null);
  private final Matcher<Object> isUnknownPattern = is(instanceOf(UnknownRecurrencePattern.class));
  private final Matcher<Object> isWeeklyPattern = is(instanceOf(WeeklyRecurrencePattern.class));

  @Test
  public void extractRecurrencePattern_MUST_return_an_unknown_recurrence_pattern_WHEN_given_no_event_samples() {
    RecurrencePattern discoveredRecurrencePattern = extractRecurrencePattern(Collections.<EventSample>emptyList());
    assertThat(discoveredRecurrencePattern, isUnknownPattern);
  }

  @Test
  public void extractRecurrencePattern_MUST_return_an_unknown_recurrence_pattern_WHEN_given_only_one_event_sample() {
    RecurrencePattern discoveredRecurrencePattern = extractRecurrencePattern(asList(eventSampleAtTime0));
    assertThat(discoveredRecurrencePattern, isUnknownPattern);
  }

  @Test
  public void extractRecurrencePattern_MUST_return_a_weekly_recurrence_pattern_WHEN_two_samples_occur_exactly_one_week_apart() {
    RecurrencePattern discoveredRecurrencePattern = extractRecurrencePattern(asList(eventSampleAtTime0, eventSampleAtTime1w));
    assertThat(discoveredRecurrencePattern, isWeeklyPattern);
  }

  @Test
  public void extractRecurrencePattern_MUST_return_a_daily_pattern_WHEN_two_samples_occur_exactly_one_day_apart() {
    RecurrencePattern discoveredRecurrencePattern = extractRecurrencePattern(asList(eventSampleAtTime0, eventSampleAtTime1d));
    assertThat(discoveredRecurrencePattern, is(instanceOf(DailyRecurrencePattern.class)));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void extractRecurrencePattern_MUST_return_a_biweekly_pattern_WHEN_events_occur_twice_a_week() {
    List<EventSample> biweeklyEventSamples = generateEventsOneWeekApart(TIME_0, 5);
    biweeklyEventSamples.addAll(generateEventsOneWeekApart(2 * MILLIS_1_DAY, 6));
    CompositeRecurrencePattern discoveredRecurrencePattern = (CompositeRecurrencePattern) extractRecurrencePattern(biweeklyEventSamples);
    assertThat(
      discoveredRecurrencePattern.getSubPatterns(),
      contains(isWeeklyPattern, isWeeklyPattern)
    );
  }

  @Test
  public void extractDailyRecurrencePattern_MUST_return_null_WHEN_given_only_a_single_event_sample() {
    assertNull(extractDailyRecurrencePattern(generateEventsOneWeekApart(TIME_0, 1)));
  }

  private ArrayList<EventSample> generateEventsOneWeekApart(long startingTime, int numberOfEventSamples) {
    ArrayList<EventSample> eventSamples = new ArrayList<EventSample>();
    for (int i = 0; i < numberOfEventSamples; i++) {
      eventSamples.add(new EventSample(EVENT_NAME, startingTime + i * numberOfEventSamples, null));
    }
    return eventSamples;
  }

}