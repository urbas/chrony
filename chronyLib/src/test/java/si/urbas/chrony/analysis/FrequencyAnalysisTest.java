package si.urbas.chrony.analysis;

import org.junit.Before;
import org.junit.Test;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.EventSample;

import java.util.Arrays;

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
  public void frequency_MUST_return_1_WHEN_the_event_occurred_once_exactly_24_hours_ago() {
    when(eventRepository.samplesOf(EVENT_NAME)).thenReturn(Arrays.asList(eventSampleAtTime0));
    assertEquals(1d, frequencyAnalysis.frequency(TIME_0, TIME_1_DAY), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void frequency_MUST_return_2_WHEN_the_event_occurred_twice_in_the_given_period() {
    when(eventRepository.samplesOf(EVENT_NAME)).thenReturn(Arrays.asList(eventSampleAtTime0, eventSampleAtTime1d));
    assertEquals(2d, frequencyAnalysis.frequency(TIME_0, TIME_1_DAY), TWO_DECIMAL_ACCURACY);
  }

  @Test
  public void allTimeFrequency_MUST_return_the_three_tenth_WHEN_three_events_occurred_in_ten_days() {
    when(eventRepository.samplesOf(EVENT_NAME)).thenReturn(Arrays.asList(eventSampleAtTime0, eventSampleAtTime1d, eventSampleAtTime10d));
    assertEquals(3.0 / 10.0, frequencyAnalysis.allTimeFrequency(), TWO_DECIMAL_ACCURACY);
  }

}