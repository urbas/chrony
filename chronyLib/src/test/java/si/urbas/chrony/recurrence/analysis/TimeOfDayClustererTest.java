package si.urbas.chrony.recurrence.analysis;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.EventSamplesTestUtils;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static si.urbas.chrony.EventSamplesTestUtils.createRecurringEventSamples;
import static si.urbas.chrony.util.TimeUtils.*;

public class TimeOfDayClustererTest {

  private TimeOfDayClusterer timeOfDayClusterer;

  @Before
  public void setUp() throws Exception {
    timeOfDayClusterer = new TimeOfDayClusterer();
  }

  @Test
  public void millisOfDayClusters_MUST_return_an_empty_list_WHEN_there_are_no_samples() {
    int[] millisOfDayClusters = timeOfDayClusterer.millisOfDayClusters(EventSamplesTestUtils.emptyEventSamples());
    assertEquals(0, millisOfDayClusters.length);
  }

  @Test
  public void millisOfDayClusters_MUST_return_a_single_time_WHEN_all_event_samples_occur_at_the_exact_same_time_of_day() {
    int millisOfDay = millisOfDay(13, 56);
    ArrayList<EventSample> eventSamples = createUniformlyRecurringEventSamples(millisOfDay);
    assertArrayEquals(
      timeOfDayClusterer.millisOfDayClusters(eventSamples),
      new int[]{millisOfDay}
    );
  }

  public static ArrayList<EventSample> createUniformlyRecurringEventSamples(int millisOfDay) {
    DateTime timeOfFirstOccurrence = createUtcDate().withMillisOfDay(millisOfDay);
    return createRecurringEventSamples(4, 4 * 10, timeOfFirstOccurrence);
  }

  public static int millisOfDay(int hour, int minute) {return (int) (hour * HOUR_IN_MILLIS + minute * MINUTE_IN_MILLIS);}

}