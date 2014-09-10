package si.urbas.chrony.analysis;

import org.junit.Before;
import org.junit.Test;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.EventSample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FrequencyAnalysisTest {

  private static final double TWO_DECIMAL_ACCURACY = 0.01;
  private static final String EVENT_NAME = "some event";
  private static final long TIME_0 = 0L;
  private static final long TIME_1_DAY = 24 * 3600 * 1000;
  private static final long TIME_10_DAY = TIME_1_DAY * 10;
  private final EventSample eventSampleAtTime0 = new EventSample(EVENT_NAME, TIME_0, null);
  private final EventSample eventSampleAtTime1d = new EventSample(EVENT_NAME, TIME_1_DAY, null);
  private final EventSample eventSampleAtTime10d = new EventSample(EVENT_NAME, TIME_10_DAY, null);
  private FrequencyAnalysis frequencyAnalysis;
  private EventRepository eventRepository;

  @Before
  public void setUp() throws Exception {
    eventRepository = mock(EventRepository.class);
    frequencyAnalysis = new FrequencyAnalysis(eventRepository, EVENT_NAME);
  }

  @Test
  public void frequency_MUST_return_0_WHEN_there_are_no_occurrences_of_the_event_at_all() {
    noSamplesInRepository();
    assertEquals(0d, frequencyAnalysis.frequency(TIME_0, TIME_1_DAY), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void frequency_MUST_return_0_WHEN_there_are_no_occurrences_of_the_event_in_the_interval() {
    addSamplesToEventRepository(Arrays.asList(eventSampleAtTime10d));
    assertEquals(0d, frequencyAnalysis.frequency(TIME_0, TIME_1_DAY), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void frequency_MUST_return_1_WHEN_the_event_occurred_once_exactly_24_hours_ago() {
    addSamplesToEventRepository(Arrays.asList(eventSampleAtTime0));
    assertEquals(1d, frequencyAnalysis.frequency(TIME_0, TIME_1_DAY), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void frequency_MUST_return_2_WHEN_the_event_occurred_twice_in_the_given_period() {
    addSamplesToEventRepository(Arrays.asList(eventSampleAtTime0, eventSampleAtTime1d));
    assertEquals(2d, frequencyAnalysis.frequency(TIME_0, TIME_1_DAY), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void frequency_MUST_return_1_WHEN_the_event_occurred_once_in_a_zero_interval() {
    addSamplesToEventRepository(Arrays.asList(eventSampleAtTime1d));
    assertEquals(1d, frequencyAnalysis.frequency(TIME_1_DAY, TIME_1_DAY), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void frequencyUntil_MUST_return_the_three_tenth_WHEN_three_events_occurred_in_ten_days() {
    threeSamplesInRepository();
    assertEquals(3.0 / 10.0, frequencyAnalysis.frequencyUntil(TIME_10_DAY), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void frequencyUntil_MUST_return_0_WHEN_there_are_no_occurrences_of_the_event_at_all() {
    noSamplesInRepository();
    assertEquals(0d, frequencyAnalysis.frequencyUntil(now()), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void frequencyUntil_MUST_return_1_WHEN_there_is_just_one_occurrence_of_the_event() {
    addSamplesToEventRepository(Arrays.asList(eventSampleAtTime10d));
    assertEquals(1d, frequencyAnalysis.frequencyUntil(TIME_10_DAY), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void occurrencesUntil_MUST_return_0_WHEN_there_are_no_events() {
    noSamplesInRepository();
    assertEquals(0, frequencyAnalysis.occurrencesUntil(now()));
  }

  @Test
  public void occurrencesUntil_MUST_return_3_WHEN_there_are_three_events_in_the_repo() {
    threeSamplesInRepository();
    assertEquals(3, frequencyAnalysis.occurrencesUntil(now()));
  }

  @Test
  public void occurrencesUntil_MUST_return_1_WHEN_there_is_only_one_event_that_falls_within_the_time_span() {
    threeSamplesInRepository();
    assertEquals(1, frequencyAnalysis.occurrencesUntil(TIME_1_DAY - 1));
  }

  @Test
  public void occurrencesWithin_MUST_return_0_WHEN_there_are_no_events() {
    noSamplesInRepository();
    assertEquals(0, frequencyAnalysis.occurrencesWithin(TIME_0, now()));
  }

  @Test
  public void occurrencesWithin_MUST_return_2_WHEN_there_are_two_events_within_the_interval() {
    threeSamplesInRepository();
    assertEquals(2, frequencyAnalysis.occurrencesWithin(TIME_1_DAY, TIME_10_DAY));
  }

  private void addSamplesToEventRepository(List<EventSample> eventSamples) {
    when(eventRepository.samplesOf(EVENT_NAME)).thenReturn(eventSamples);
  }

  private void noSamplesInRepository() {
    addSamplesToEventRepository(new ArrayList<EventSample>());
  }

  private void threeSamplesInRepository() {
    addSamplesToEventRepository(Arrays.asList(eventSampleAtTime0, eventSampleAtTime1d, eventSampleAtTime10d));
  }

  private static long now() {
    return new Date().getTime();
  }

}