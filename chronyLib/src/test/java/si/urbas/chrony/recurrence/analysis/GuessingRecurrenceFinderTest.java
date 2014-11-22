package si.urbas.chrony.recurrence.analysis;

import org.junit.Before;
import org.junit.Test;
import si.urbas.chrony.EventSample;

import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.EventSamplesTestUtils.createRandomEventSamples;
import static si.urbas.chrony.EventSamplesTestUtils.emptyEventSamples;
import static si.urbas.chrony.recurrence.test.matchers.RecurrenceMatchers.recurrence;
import static si.urbas.chrony.util.EventSampleUtils.oldestTimestamp;
import static si.urbas.chrony.util.TimeUtils.HOUR_IN_MILLIS;
import static si.urbas.chrony.util.TimeUtils.MINUTE_IN_MILLIS;

public class GuessingRecurrenceFinderTest {

  private GuessingRecurrenceFinder recurrenceFinder;
  private Random randomnessSource;

  @Before
  public void setUp() throws Exception {
    recurrenceFinder = new GuessingRecurrenceFinder();
    randomnessSource = new Random(3492823927L);
  }

  @Test
  public void foundRecurrences_MUST_be_empty_WHEN_there_are_fewer_than_two_event_samples() {
    assertThat(recurrenceFinder.foundRecurrences(emptyEventSamples()), is(empty()));
  }

  @Test
  public void foundRecurrences_MUST_return_a_single_recurrence_WHEN_event_samples_recur_with_a_constant_period_and_phase() {
    List<EventSample> eventSamples = createRandomEventSamples(randomnessSource, 3, 30, HOUR_IN_MILLIS, 2014, 10, 23, 16, 9);
    assertThat(
      recurrenceFinder.foundRecurrences(eventSamples),
      contains(recurrence().within(10 * MINUTE_IN_MILLIS).of(oldestTimestamp(eventSamples)).withPeriodInDays(3))
    );
  }

}