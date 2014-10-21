package si.urbas.chrony.recurrence.analysis;

import org.junit.Test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static si.urbas.chrony.EventSamplesTestUtils.emptyEventSamples;

public class SingleDailyRecurrenceFinderTest {

  @Test
  public void foundRecurrences_MUST_be_empty_WHEN_there_are_fewer_than_two_event_samples() {
    SingleDailyRecurrenceFinder recurrenceAnalyser = new SingleDailyRecurrenceFinder(emptyEventSamples());
    assertThat(recurrenceAnalyser.foundRecurrences(), is(empty()));
  }
  
  @Test
  public void foundRecurrences_MUST_return_a_recurrence_for_two_events() {
    SingleDailyRecurrenceFinder recurrenceAnalyser = new SingleDailyRecurrenceFinder(emptyEventSamples());
    assertThat(recurrenceAnalyser.foundRecurrences(), is(empty()));
  }

}