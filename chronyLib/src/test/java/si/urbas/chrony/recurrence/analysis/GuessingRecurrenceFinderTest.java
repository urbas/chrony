package si.urbas.chrony.recurrence.analysis;

import org.junit.Test;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.EventSamplesTestUtils.emptyEventSamples;

public class GuessingRecurrenceFinderTest {

  @Test
  public void foundRecurrences_MUST_be_empty_WHEN_there_are_fewer_than_two_event_samples() {
    GuessingRecurrenceFinder recurrenceAnalyser = new GuessingRecurrenceFinder(emptyEventSamples());
    assertThat(recurrenceAnalyser.foundRecurrences(), is(empty()));
  }

  @Test
  public void foundRecurrences_MUST_() {

  }

}