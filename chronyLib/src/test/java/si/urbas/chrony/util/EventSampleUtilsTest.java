package si.urbas.chrony.util;

import org.joda.time.DateTime;
import org.junit.Test;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.EventSamplesTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.EventSamplesTestUtils.createRecurringEventSamples;
import static si.urbas.chrony.EventSamplesTestUtils.emptyEventSamples;
import static si.urbas.chrony.util.EventSampleUtils.*;
import static si.urbas.chrony.util.TimeUtils.*;

public class EventSampleUtilsTest {

  private static final EventSample eventSampleAt12h45m12s = EventSamplesTestUtils.eventSampleAt(toUtcTimeInMillis(2014, 9, 29, 12, 45, 12));
  private static final EventSample eventSampleAt17h45m12s = EventSamplesTestUtils.eventSampleAt(toUtcTimeInMillis(2014, 9, 29, 17, 45, 12));
  private static final EventSample eventSampleAt07h45m12s = EventSamplesTestUtils.eventSampleAt(toUtcTimeInMillis(2014, 9, 29, 7, 45, 12));
  private static final EventSample eventSampleAt12h45m42s = EventSamplesTestUtils.eventSampleAt(toUtcTimeInMillis(2014, 9, 29, 12, 45, 42));
  private static final EventSample eventSampleAt12h44m42s = EventSamplesTestUtils.eventSampleAt(toUtcTimeInMillis(2014, 9, 29, 12, 44, 42));
  private static final EventSample eventSampleAt12h20m12s = EventSamplesTestUtils.eventSampleAt(toUtcTimeInMillis(2014, 9, 29, 12, 20, 12));
  private static final EventSample eventSampleAt13h10m12s = EventSamplesTestUtils.eventSampleAt(toUtcTimeInMillis(2014, 9, 29, 13, 10, 12));
  private static final List<EventSample> eventSamples = EventSampleUtils.sortByTimestamp(Arrays.asList(eventSampleAt07h45m12s, eventSampleAt17h45m12s,
                                                                                                       eventSampleAt12h45m42s, eventSampleAt12h44m42s,
                                                                                                       eventSampleAt12h20m12s, eventSampleAt13h10m12s,
                                                                                                       eventSampleAt12h45m12s));

  @Test
  public void countSamplesWithinTime_MUST_return_0_WHEN_bounds_are_before_all_the_event_samples() {
    long fromTime = oldestTimestamp(eventSamples) - 1;
    long untilTime = oldestTimestamp(eventSamples) - 1;
    assertEquals(0, countSamplesWithinTime(eventSamples, fromTime, untilTime));
  }

  @Test
  public void countSamplesWithinTime_MUST_return_the_size_of_the_list_WHEN_bounds_encompass_the_entire_list_of_samples() {
    long fromTime = oldestTimestamp(eventSamples) - 1;
    long untilTime = newestTimestamp(eventSamples) + 1;
    assertEquals(eventSamples.size(), countSamplesWithinTime(eventSamples, fromTime, untilTime));
  }

  @Test
  public void countSamplesWithinTime_MUST_return_1_WHEN_bounds_encompass_the_first_element() {
    long fromTime = oldestTimestamp(eventSamples) - 1;
    long untilTime = oldestTimestamp(eventSamples) + 1;
    assertEquals(1, countSamplesWithinTime(eventSamples, fromTime, untilTime));
  }

  @Test
  public void countSamplesWithinTime_MUST_return_1_WHEN_bounds_fall_on_the_first_element() {
    long fromTime = oldestTimestamp(eventSamples);
    long untilTime = oldestTimestamp(eventSamples);
    assertEquals(1, countSamplesWithinTime(eventSamples, fromTime, untilTime));
  }

  @Test
  public void countSamplesWithinTime_MUST_return_the_size_of_the_list_WHEN_bounds_fall_on_the_bounding_elements() {
    long fromTime = oldestTimestamp(eventSamples);
    long untilTime = newestTimestamp(eventSamples);
    assertEquals(eventSamples.size(), countSamplesWithinTime(eventSamples, fromTime, untilTime));
  }

  @Test
  public void countSamplesWithinTime_MUST_return_the_count_of_encompassed_elements() {
    long fromTime = eventSamples.get(1).getTimestampInMillis();
    long untilTime = eventSamples.get(3).getTimestampInMillis();
    assertEquals(3, countSamplesWithinTime(eventSamples, fromTime, untilTime));
  }

  @Test
  public void countSamplesWithinTime_MUST_return_the_count_of_encompassed_elements_WHEN_the_upper_bound_is_beyond() {
    long fromTime = eventSamples.get(1).getTimestampInMillis();
    long untilTime = eventSamples.get(3).getTimestampInMillis() + 1;
    assertEquals(3, countSamplesWithinTime(eventSamples, fromTime, untilTime));
  }

  @Test
  public void countSamplesWithinTime_MUST_return_the_count_of_encompassed_elements_WHEN_the_lower_bound_is_beyond() {
    long fromTime = eventSamples.get(1).getTimestampInMillis() - 1;
    long untilTime = eventSamples.get(3).getTimestampInMillis();
    assertEquals(3, countSamplesWithinTime(eventSamples, fromTime, untilTime));
  }

  @Test
  public void countSamplesWithinTime_MUST_return_the_count_of_encompassed_elements_WHEN_both_bounds_are_beyond() {
    long fromTime = eventSamples.get(1).getTimestampInMillis() - 1;
    long untilTime = eventSamples.get(3).getTimestampInMillis() + 1;
    assertEquals(3, countSamplesWithinTime(eventSamples, fromTime, untilTime));
  }

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
    assertEquals(expectedAverage, averageTimeOfDay(eventSamples));
  }

  @Test
  public void findClosest_MUST_return_the_first_event_sample_WHEN_given_a_timestamp_smaller_than_the_timestamp_of_first_sample() {
    assertEquals(
      eventSampleAt07h45m12s,
      findClosest(eventSamples, eventSampleAt07h45m12s.getTimestamp().minusMillis(1).getMillis())
    );
  }

  @Test
  public void findClosest_MUST_return_the_first_event_sample_WHEN_given_the_timestamp_of_the_sample() {
    assertEquals(
      eventSampleAt07h45m12s,
      findClosest(eventSamples, eventSampleAt07h45m12s.getTimestamp().getMillis())
    );
  }

  @Test
  public void findClosest_MUST_return_the_last_event_sample_WHEN_the_timestamp_greater_than_the_last_sample() {
    assertEquals(
      eventSampleAt17h45m12s,
      findClosest(eventSamples, eventSampleAt17h45m12s.getTimestamp().plusMillis(1).getMillis())
    );
  }

  @Test
  public void findClosest_MUST_return_the_first_event_sample_WHEN_the_timestamp_closer_to_it() {
    assertEquals(
      eventSampleAt07h45m12s,
      findClosest(eventSamples, eventSampleAt07h45m12s.getTimestamp().plusMillis(1).getMillis())
    );
  }

  @Test
  public void findClosest_MUST_return_the_second_event_sample_WHEN_the_timestamp_closer_to_it() {
    assertEquals(
      eventSampleAt12h20m12s,
      findClosest(eventSamples, eventSampleAt12h20m12s.getTimestamp().minusMillis(1).getMillis())
    );
  }

  @Test
  public void merge_MUST_return_an_empty_list_WHEN_both_lists_are_empty() {
    assertThat(merge(emptyEventSamples(), emptyEventSamples()), is(empty()));
  }

  @Test
  public void merge_MUST_return_the_first_list_WHEN_the_second_one_is_empty() {
    ArrayList<EventSample> eventSamples = createRecurringEventSamples(3, 30, createUtcDate());
    assertThat(merge(eventSamples, emptyEventSamples()), equalTo(eventSamples));
  }

  @Test
  public void merge_MUST_return_the_second_list_WHEN_the_first_one_is_empty() {
    ArrayList<EventSample> eventSamples = createRecurringEventSamples(3, 30, createUtcDate());
    assertThat(merge(emptyEventSamples(), eventSamples), equalTo(eventSamples));
  }

  @Test
  public void merge_MUST_add_the_first_list_to_the_back_WHEN_all_samples_happen_after_samples_in_the_second_list() {
    ArrayList<EventSample> eventSamplesA = createRecurringEventSamples(3, 6, createUtcDate().plusDays(7));
    ArrayList<EventSample> eventSamplesB = createRecurringEventSamples(3, 6, createUtcDate());
    ArrayList<EventSample> expectedEventSamples = new ArrayList<EventSample>(eventSamplesB);
    expectedEventSamples.addAll(eventSamplesA);
    assertThat(merge(eventSamplesA, eventSamplesB), equalTo(expectedEventSamples));
  }

  @Test
  public void merge_MUST_insert_the_first_list_into_the_middle() {
    ArrayList<EventSample> eventSamplesA = createRecurringEventSamples(3, 1, createUtcDate().plusDays(1));
    ArrayList<EventSample> eventSamplesB = createRecurringEventSamples(5, 5, createUtcDate());
    assertThat(
      merge(eventSamplesA, eventSamplesB),
      contains(eventSamplesB.get(0), eventSamplesA.get(0), eventSamplesB.get(1))
    );
  }

  @Test
  public void merge_MUST_insert_the_second_list_into_the_middle() {
    ArrayList<EventSample> eventSamplesA = createRecurringEventSamples(5, 5, createUtcDate());
    ArrayList<EventSample> eventSamplesB = createRecurringEventSamples(3, 1, createUtcDate().plusDays(1));
    assertThat(
      merge(eventSamplesA, eventSamplesB),
      contains(eventSamplesA.get(0), eventSamplesB.get(0), eventSamplesA.get(1))
    );
  }

  @Test
  public void merge_MUST_interleaved_samples() {
    DateTime now = createUtcDate();
    ArrayList<EventSample> eventSamplesA = createRecurringEventSamples(5, 15, now);
    ArrayList<EventSample> eventSamplesB = createRecurringEventSamples(3, 18, now.plusDays(1));
    assertThat(
      merge(eventSamplesA, eventSamplesB),
      equalTo(asList(
        eventSamplesA.get(0),
        eventSamplesB.get(0),
        eventSamplesB.get(1),
        eventSamplesA.get(1),
        eventSamplesB.get(2),
        eventSamplesA.get(2),
        eventSamplesB.get(3),
        eventSamplesB.get(4),
        eventSamplesA.get(3),
        eventSamplesB.get(5),
        eventSamplesB.get(6)
      ))
    );
  }

}