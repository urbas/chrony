package si.urbas.chrony.util;

import org.junit.Test;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.EventSamplesTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static si.urbas.chrony.util.EventSampleUtils.averageTimeOfDay;
import static si.urbas.chrony.util.TimeUtils.*;

public class EventSampleUtilsTest {

  private final EventSample eventSampleAt12h45m12s = EventSamplesTestUtils.eventSampleAt(toUtcTimeInMillis(2014, 9, 29, 12, 45, 12));
  private final EventSample eventSampleAt17h45m12s = EventSamplesTestUtils.eventSampleAt(toUtcTimeInMillis(2014, 9, 29, 17, 45, 12));
  private final EventSample eventSampleAt07h45m12s = EventSamplesTestUtils.eventSampleAt(toUtcTimeInMillis(2014, 9, 29, 7, 45, 12));
  private final EventSample eventSampleAt12h45m42s = EventSamplesTestUtils.eventSampleAt(toUtcTimeInMillis(2014, 9, 29, 12, 45, 42));
  private final EventSample eventSampleAt12h44m42s = EventSamplesTestUtils.eventSampleAt(toUtcTimeInMillis(2014, 9, 29, 12, 44, 42));
  private final EventSample eventSampleAt12h20m12s = EventSamplesTestUtils.eventSampleAt(toUtcTimeInMillis(2014, 9, 29, 12, 20, 12));
  private final EventSample eventSampleAt13h10m12s = EventSamplesTestUtils.eventSampleAt(toUtcTimeInMillis(2014, 9, 29, 13, 10, 12));

  @Test(expected = IllegalArgumentException.class)
  public void averageTimeOfDay_MUST_throw_an_exception_WHEN_there_are_no_event_samples() {
    averageTimeOfDay(Collections.<EventSample>emptyList());
  }

  @Test
  public void averageTimeOfDay_MUST_return_the_time_of_day_of_the_single_event_sample() {
    long expectedAverage = 12 * HOUR_IN_MILLIS + 45 * MINUTE_IN_MILLIS + 12 * SECOND_IN_MILLIS;
    assertEquals(expectedAverage, averageTimeOfDay(Arrays.asList(eventSampleAt12h45m12s)));
  }

  @Test
  public void averageTimeOfDay_MUST_return_average_of_times_of_day_of_all_event_samples() {
    long expectedAverage = 12 * HOUR_IN_MILLIS + 45 * MINUTE_IN_MILLIS + 12 * SECOND_IN_MILLIS;
    List<EventSample> eventSamples = Arrays.asList(eventSampleAt07h45m12s, eventSampleAt17h45m12s,
                                                   eventSampleAt12h45m42s, eventSampleAt12h44m42s,
                                                   eventSampleAt12h20m12s, eventSampleAt13h10m12s,
                                                   eventSampleAt12h45m12s);
    assertEquals(expectedAverage, averageTimeOfDay(eventSamples));
  }

}