package si.urbas.chrony.analysis;

import org.hamcrest.Matcher;
import org.junit.Test;
import si.urbas.chrony.EventSample;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.util.TimeUtils.*;

public class RecurrenceAnalysisTest {


  private static final String EVENT_NAME = "event name";
  private final EventSample eventSampleAtTime0 = new EventSample(EVENT_NAME, TIME_0, null);
  private final EventSample eventSampleAtTime1d = new EventSample(EVENT_NAME, MILLIS_1_DAY, null);
  private final Matcher<RecurrencePattern> isDailyPattern = instanceOf(DailyRecurrencePattern.class);

  @Test
  public void foundPatterns_MUST_return_an_empty_list_WHEN_given_no_event_samples() {
    RecurrenceAnalysis recurrenceAnalysis = new RecurrenceAnalysis(Collections.<EventSample>emptyList());
    assertThat(recurrenceAnalysis.foundPatterns(), is(empty()));
  }

  @Test
  public void foundPatterns_MUST_return_an_empty_list_WHEN_given_a_single_event_sample() {
    RecurrenceAnalysis recurrenceAnalysis = new RecurrenceAnalysis(asList(eventSampleAtTime0));
    assertThat(recurrenceAnalysis.foundPatterns(), is(empty()));
  }

  @Test
  public void foundPatterns_MUST_return_a_daily_pattern_WHEN_given_two_events_a_day_apart() {
    RecurrenceAnalysis recurrenceAnalysis = new RecurrenceAnalysis(asList(eventSampleAtTime0, eventSampleAtTime1d));
    assertThat(recurrenceAnalysis.foundPatterns(), hasItem(isDailyPattern));
  }

}