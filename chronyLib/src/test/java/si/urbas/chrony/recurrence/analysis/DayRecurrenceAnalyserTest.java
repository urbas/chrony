package si.urbas.chrony.recurrence.analysis;

import org.junit.Test;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.EventSamplesTestUtils;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

@SuppressWarnings("unchecked")
public class DayRecurrenceAnalyserTest extends RecurrenceAnalyserTest {

  @Override
  protected RecurrenceAnalyser createRecurrenceAnalyser(List<EventSample> eventSamples) {
    return new DayRecurrenceAnalyser(eventSamples);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructor_MUST_throw_an_exception_WHEN_the_list_of_event_samples_is_not_sorted() {
    createRecurrenceAnalyser(asList(eventSampleAtTime2d17h, eventSampleAtTime1d17h));
  }

  @Test
  public void foundPatterns_MUST_return_two_weekly_patterns_WHEN_given_four_events() {
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(asList(eventSampleAtTime1d17h, eventSampleAtTime3d17h, eventSampleAtTime8d17h, eventSampleAtTime10d17h));
    assertThat(recurrenceAnalyser.foundRecurrences().getRecurrences(), contains(weeklyRecurrence, weeklyRecurrence));
  }

  @Test
  public void foundPatterns_MUST_return_two_weekly_patterns_WHEN_they_pairwise_happen_on_same_weekday_but_at_different_time() {
    List<EventSample> eventSamples = asList(EventSamplesTestUtils.eventSampleAtTime(1, 9), EventSamplesTestUtils.eventSampleAtTime(1, 20), EventSamplesTestUtils.eventSampleAtTime(8, 9), EventSamplesTestUtils.eventSampleAtTime(8, 20));
    RecurrenceAnalyser recurrenceAnalyser = createRecurrenceAnalyser(eventSamples);
    assertThat(recurrenceAnalyser.foundRecurrences().getRecurrences(), contains(weeklyRecurrence, weeklyRecurrence));
  }

}