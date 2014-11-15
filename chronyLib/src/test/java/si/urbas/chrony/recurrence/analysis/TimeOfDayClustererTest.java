package si.urbas.chrony.recurrence.analysis;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.EventSamplesTestUtils;
import si.urbas.chrony.util.EventSampleUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static si.urbas.chrony.EventSamplesTestUtils.createRandomEventSamples;
import static si.urbas.chrony.EventSamplesTestUtils.createRecurringEventSamples;
import static si.urbas.chrony.util.EventSampleUtils.averageTimeOfDay;
import static si.urbas.chrony.util.TimeUtils.*;

public class TimeOfDayClustererTest {

  private TimeOfDayClusterer timeOfDayClusterer;
  private static final int TIME_13_56_IN_MILLIS = millisOfDay(13, 56);
  private static final int TIME_17_13_IN_MILLIS = millisOfDay(17, 13);
  private Random randomnessSource;

  @Before
  public void setUp() throws Exception {
    timeOfDayClusterer = new TimeOfDayClusterer();
    randomnessSource = new Random(349238237);
  }

  @Test
  public void millisOfDayClusters_MUST_return_an_empty_list_WHEN_there_are_no_samples() {
    int[] millisOfDayClusters = timeOfDayClusterer.millisOfDayClusters(EventSamplesTestUtils.emptyEventSamples());
    assertEquals(0, millisOfDayClusters.length);
  }

  @Test
  public void millisOfDayClusters_MUST_return_a_single_time_WHEN_all_event_samples_occur_at_the_exact_same_time_of_day() {
    assertArrayEquals(
      toIntArray(TIME_13_56_IN_MILLIS),
      timeOfDayClusterer.millisOfDayClusters(someEventSamplesAt(TIME_13_56_IN_MILLIS))
    );
  }

  @Test
  public void millisOfDayClusters_MUST_return_an_averages_time() {
    List<EventSample> eventSamples = someEventSamplesRoughlyAt(TIME_13_56_IN_MILLIS);
    int averageTimeOfDayInMillis = averageTimeOfDay(eventSamples);
    assertArrayEquals(
      toIntArray(averageTimeOfDayInMillis),
      timeOfDayClusterer.millisOfDayClusters(eventSamples)
    );
  }

  @Test
  public void millisOfDayClusters_MUST_find_two_clusters() {
    List<EventSample> eventSamples13_56 = someEventSamplesRoughlyAt(TIME_13_56_IN_MILLIS);
    List<EventSample> eventSamples17_13 = someEventSamplesRoughlyAt(TIME_17_13_IN_MILLIS);

    assertArrayEquals(
      toIntArray(averageTimeOfDay(eventSamples13_56), averageTimeOfDay(eventSamples17_13)),
      timeOfDayClusterer.millisOfDayClusters(EventSampleUtils.merge(eventSamples13_56, eventSamples17_13))
    );
  }

  private List<EventSample> someEventSamplesRoughlyAt(int timeOfDayInMillis) {
    return createRandomEventSamples(randomnessSource, 3, 3 * 25, 10 * MINUTE_IN_MILLIS, timeOfDayInMillis);
  }

  private static ArrayList<EventSample> someEventSamplesAt(int millisOfDay) {
    DateTime timeOfFirstOccurrence = createUtcDate().withMillisOfDay(millisOfDay);
    return createRecurringEventSamples(4, 4 * 10, timeOfFirstOccurrence);
  }

  public static int[] toIntArray(int... ints) {return ints;}

  public static int millisOfDay(int hour, int minute) {return (int) (hour * HOUR_IN_MILLIS + minute * MINUTE_IN_MILLIS);}

}