package si.urbas.chrony.collections;

import org.joda.time.DateTime;
import org.junit.Test;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.util.EventSampleTimestampComparator;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static si.urbas.chrony.EventSamplesTestUtils.createRecurringEventSamples;
import static si.urbas.chrony.EventSamplesTestUtils.emptyEventSamples;
import static si.urbas.chrony.util.TimeUtils.createUtcDate;

public class IterablesTest {

  private static final EventSampleTimestampComparator EVENT_SAMPLE_COMPARATOR = EventSampleTimestampComparator.INSTANCE;


  @Test
  public void merge_MUST_return_an_empty_list_WHEN_both_lists_are_empty() {
    assertThat(Iterables.merge(emptyEventSamples(), emptyEventSamples(), EVENT_SAMPLE_COMPARATOR), is(empty()));
  }

  @Test
  public void merge_MUST_return_the_first_list_WHEN_the_second_one_is_empty() {
    ArrayList<EventSample> eventSamples = createRecurringEventSamples(3, 30, createUtcDate());
    assertThat(Iterables.merge(eventSamples, emptyEventSamples(), EVENT_SAMPLE_COMPARATOR), equalTo(eventSamples));
  }

  @Test
  public void merge_MUST_return_the_second_list_WHEN_the_first_one_is_empty() {
    ArrayList<EventSample> eventSamples = createRecurringEventSamples(3, 30, createUtcDate());
    assertThat(Iterables.merge(emptyEventSamples(), eventSamples, EVENT_SAMPLE_COMPARATOR), equalTo(eventSamples));
  }

  @Test
  public void merge_MUST_add_the_first_list_to_the_back_WHEN_all_samples_happen_after_samples_in_the_second_list() {
    ArrayList<EventSample> eventSamplesA = createRecurringEventSamples(3, 6, createUtcDate().plusDays(7));
    ArrayList<EventSample> eventSamplesB = createRecurringEventSamples(3, 6, createUtcDate());
    ArrayList<EventSample> expectedEventSamples = new ArrayList<EventSample>(eventSamplesB);
    expectedEventSamples.addAll(eventSamplesA);
    assertThat(Iterables.merge(eventSamplesA, eventSamplesB, EVENT_SAMPLE_COMPARATOR), equalTo(expectedEventSamples));
  }

  @Test
  public void merge_MUST_insert_the_first_list_into_the_middle() {
    ArrayList<EventSample> eventSamplesA = createRecurringEventSamples(3, 1, createUtcDate().plusDays(1));
    ArrayList<EventSample> eventSamplesB = createRecurringEventSamples(5, 5, createUtcDate());
    assertThat(
      Iterables.merge(eventSamplesA, eventSamplesB, EVENT_SAMPLE_COMPARATOR),
      contains(eventSamplesB.get(0), eventSamplesA.get(0), eventSamplesB.get(1))
    );
  }

  @Test
  public void merge_MUST_insert_the_second_list_into_the_middle() {
    ArrayList<EventSample> eventSamplesA = createRecurringEventSamples(5, 5, createUtcDate());
    ArrayList<EventSample> eventSamplesB = createRecurringEventSamples(3, 1, createUtcDate().plusDays(1));
    assertThat(
      Iterables.merge(eventSamplesA, eventSamplesB, EVENT_SAMPLE_COMPARATOR),
      contains(eventSamplesA.get(0), eventSamplesB.get(0), eventSamplesA.get(1))
    );
  }

  @Test
  public void merge_MUST_interleaved_samples() {
    DateTime now = createUtcDate();
    ArrayList<EventSample> eventSamplesA = createRecurringEventSamples(5, 15, now);
    ArrayList<EventSample> eventSamplesB = createRecurringEventSamples(3, 18, now.plusDays(1));
    assertThat(
      Iterables.merge(eventSamplesA, eventSamplesB, EVENT_SAMPLE_COMPARATOR),
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