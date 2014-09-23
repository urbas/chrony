package si.urbas.chrony.analysis;

import org.junit.Test;
import si.urbas.chrony.EventSample;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.util.TimeUtils.DAY_IN_MILLIS;
import static si.urbas.chrony.util.TimeUtils.TIME_0;

public abstract class RecurrenceAnalyserTest {

  protected static final String EVENT_NAME = "event name";
  protected final EventSample eventSampleAtTime9d = new EventSample(EVENT_NAME, 9 * DAY_IN_MILLIS, null);
  protected final EventSample eventSampleAtTime0 = new EventSample(EVENT_NAME, TIME_0, null);

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

  protected abstract RecurrenceAnalyser createRecurrenceAnalyser(List<EventSample> eventSamples);
}
