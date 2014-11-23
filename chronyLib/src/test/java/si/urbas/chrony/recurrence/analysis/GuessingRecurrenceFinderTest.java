package si.urbas.chrony.recurrence.analysis;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import si.urbas.chrony.EventSample;

import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.EventSamplesTestUtils.createRandomEventSamples;
import static si.urbas.chrony.EventSamplesTestUtils.emptyEventSamples;
import static si.urbas.chrony.recurrence.test.matchers.RecurrenceMatchers.recurrence;
import static si.urbas.chrony.util.EventSampleUtils.merge;
import static si.urbas.chrony.util.TimeUtils.*;

public class GuessingRecurrenceFinderTest {

  private static final long TIME_OF_DAY_TOLERANCE = 50 * MINUTE_IN_MILLIS;
  private GuessingRecurrenceFinder recurrenceFinder;
  private Random randomnessSource;
  private long occurrenceAt16h;
  private long occurrenceAt16hAfter1Day;
  private long occurrenceAt19h;

  @Before
  public void setUp() throws Exception {
    recurrenceFinder = new GuessingRecurrenceFinder();
    randomnessSource = new Random(3492823927L);
    occurrenceAt16h = toUtcTimeInMillis(2014, 10, 23, 16, 9, 0);
    occurrenceAt16hAfter1Day = toUtcTimeInMillis(2014, 10, 24, 16, 9, 0);
    occurrenceAt19h = toUtcTimeInMillis(2014, 10, 23, 19, 9, 0);
  }

  @Test
  public void foundRecurrences_MUST_be_empty_WHEN_there_are_fewer_than_two_event_samples() {
    assertThat(recurrenceFinder.foundRecurrences(emptyEventSamples()), is(empty()));
  }

  @Test
  public void foundRecurrences_MUST_return_a_single_recurrence_with_the_right_time_of_day_and_period() {
    List<EventSample> eventSamples = createRandomEventSamples(randomnessSource, 3, 30, HOUR_IN_MILLIS, occurrenceAt16h);
    assertThat(
      recurrenceFinder.foundRecurrences(eventSamples),
      hasItem(recurrence().within(TIME_OF_DAY_TOLERANCE).of(occurrenceAt16h).withPeriodInDays(3))
    );
  }

  @Test
  public void foundRecurrences_MUST_return_two_recurrences_with_different_times_of_days() {
    List<EventSample> eventSamples = merge(
      createRandomEventSamples(randomnessSource, 3, 30, HOUR_IN_MILLIS, occurrenceAt16h),
      createRandomEventSamples(randomnessSource, 3, 30, HOUR_IN_MILLIS, occurrenceAt19h)
    );
    assertThat(
      recurrenceFinder.foundRecurrences(eventSamples),
      hasItems(
        recurrence().within(TIME_OF_DAY_TOLERANCE).of(occurrenceAt16h).withPeriodInDays(3),
        recurrence().within(TIME_OF_DAY_TOLERANCE).of(occurrenceAt19h).withPeriodInDays(3)
      )
    );
  }

  @Test
  @Ignore
  public void foundRecurrences_MUST_return_two_recurrences_with_different_phases() {
    List<EventSample> eventSamples = merge(
      createRandomEventSamples(randomnessSource, 3, 30, HOUR_IN_MILLIS, occurrenceAt16h),
      createRandomEventSamples(randomnessSource, 3, 30, HOUR_IN_MILLIS, occurrenceAt16hAfter1Day)
    );
    assertThat(
      recurrenceFinder.foundRecurrences(eventSamples),
      hasItems(
        recurrence().within(TIME_OF_DAY_TOLERANCE).of(occurrenceAt16h).withPeriodInDays(3),
        recurrence().within(TIME_OF_DAY_TOLERANCE).of(occurrenceAt16hAfter1Day).withPeriodInDays(3)
      )
    );
  }

}