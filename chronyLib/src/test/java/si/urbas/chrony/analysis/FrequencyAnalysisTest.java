package si.urbas.chrony.analysis;

import org.junit.Test;
import si.urbas.chrony.EventSample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static si.urbas.chrony.util.TimeUtils.DAY_IN_MILLIS;
import static si.urbas.chrony.util.TimeUtils.TIME_0;

public class FrequencyAnalysisTest {

  private static final double TWO_DECIMAL_ACCURACY = 0.01;
  private static final String EVENT_NAME = "some event";
  private static final long MILLIS_10_DAYS = 10 * DAY_IN_MILLIS;
  private final EventSample eventSampleAtTime0 = new EventSample(EVENT_NAME, TIME_0, null);
  private final EventSample eventSampleAtTime1d = new EventSample(EVENT_NAME, DAY_IN_MILLIS, null);
  private final EventSample eventSampleAtTime10d = new EventSample(EVENT_NAME, MILLIS_10_DAYS, null);

  @Test
  public void frequency_MUST_return_0_WHEN_there_are_no_occurrences_of_the_event_at_all() {
    FrequencyAnalysis frequencyAnalysis = noSamplesInRepository();
    assertEquals(0d, frequencyAnalysis.frequency(TIME_0, DAY_IN_MILLIS), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void frequency_MUST_return_0_WHEN_there_are_no_occurrences_of_the_event_in_the_interval() {
    FrequencyAnalysis frequencyAnalysis = addSamplesToEventRepository(Arrays.asList(eventSampleAtTime10d));
    assertEquals(0d, frequencyAnalysis.frequency(TIME_0, DAY_IN_MILLIS), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void frequency_MUST_return_1_WHEN_the_event_occurred_once_exactly_24_hours_ago() {
    FrequencyAnalysis frequencyAnalysis = addSamplesToEventRepository(Arrays.asList(eventSampleAtTime0));
    assertEquals(1d, frequencyAnalysis.frequency(TIME_0, DAY_IN_MILLIS), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void frequency_MUST_return_2_WHEN_the_event_occurred_twice_in_the_given_period() {
    FrequencyAnalysis frequencyAnalysis = addSamplesToEventRepository(Arrays.asList(eventSampleAtTime0, eventSampleAtTime1d));
    assertEquals(2d, frequencyAnalysis.frequency(TIME_0, DAY_IN_MILLIS), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void frequency_MUST_return_1_WHEN_the_event_occurred_once_in_a_zero_interval() {
    FrequencyAnalysis frequencyAnalysis = addSamplesToEventRepository(Arrays.asList(eventSampleAtTime1d));
    assertEquals(1d, frequencyAnalysis.frequency(DAY_IN_MILLIS, DAY_IN_MILLIS), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void frequencyUntil_MUST_return_the_three_tenth_WHEN_three_events_occurred_in_ten_days() {
    FrequencyAnalysis frequencyAnalysis = threeSamplesInRepository();
    assertEquals(3.0 / 10.0, frequencyAnalysis.frequencyUntil(MILLIS_10_DAYS), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void frequencyUntil_MUST_return_0_WHEN_there_are_no_occurrences_of_the_event_at_all() {
    FrequencyAnalysis frequencyAnalysis = noSamplesInRepository();
    assertEquals(0d, frequencyAnalysis.frequencyUntil(now()), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void frequencyUntil_MUST_return_1_WHEN_there_is_just_one_occurrence_of_the_event() {
    FrequencyAnalysis frequencyAnalysis = addSamplesToEventRepository(Arrays.asList(eventSampleAtTime10d));
    assertEquals(1d, frequencyAnalysis.frequencyUntil(MILLIS_10_DAYS), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void occurrencesUntil_MUST_return_0_WHEN_there_are_no_events() {
    FrequencyAnalysis frequencyAnalysis = noSamplesInRepository();
    assertEquals(0, frequencyAnalysis.occurrencesUntil(now()));
  }

  @Test
  public void occurrencesUntil_MUST_return_3_WHEN_there_are_three_events_in_the_repo() {
    FrequencyAnalysis frequencyAnalysis = threeSamplesInRepository();
    assertEquals(3, frequencyAnalysis.occurrencesUntil(now()));
  }

  @Test
  public void occurrencesUntil_MUST_return_1_WHEN_there_is_only_one_event_that_falls_within_the_time_span() {
    FrequencyAnalysis frequencyAnalysis = threeSamplesInRepository();
    assertEquals(1, frequencyAnalysis.occurrencesUntil(DAY_IN_MILLIS - 1));
  }

  @Test
  public void occurrencesWithin_MUST_return_0_WHEN_there_are_no_events() {
    FrequencyAnalysis frequencyAnalysis = noSamplesInRepository();
    assertEquals(0, frequencyAnalysis.occurrencesWithin(TIME_0, now()));
  }

  @Test
  public void occurrencesWithin_MUST_return_2_WHEN_there_are_two_events_within_the_interval() {
    FrequencyAnalysis frequencyAnalysis = threeSamplesInRepository();
    assertEquals(2, frequencyAnalysis.occurrencesWithin(DAY_IN_MILLIS, MILLIS_10_DAYS));
  }

  private FrequencyAnalysis addSamplesToEventRepository(List<EventSample> eventSamples) {
    return new FrequencyAnalysis(eventSamples);
  }

  private FrequencyAnalysis noSamplesInRepository() {
    return addSamplesToEventRepository(new ArrayList<EventSample>());
  }

  private FrequencyAnalysis threeSamplesInRepository() {
    return addSamplesToEventRepository(Arrays.asList(eventSampleAtTime0, eventSampleAtTime1d, eventSampleAtTime10d));
  }

  private static long now() {
    return new Date().getTime();
  }

}