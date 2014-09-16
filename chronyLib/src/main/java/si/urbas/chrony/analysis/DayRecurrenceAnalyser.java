package si.urbas.chrony.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.util.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static si.urbas.chrony.util.EventSampleAssertions.assertEventSamplesOrdered;

public class DayRecurrenceAnalyser {

  private final List<EventSample> eventSamples;

  /**
   * @param eventSamples this list must be ordered by the timestamp of the event samples (increasing order).
   */
  public DayRecurrenceAnalyser(List<EventSample> eventSamples) {
    assertEventSamplesOrdered(eventSamples);
    this.eventSamples = eventSamples;
  }

  public List<DailyRecurrencePattern> foundPatterns() {
    if (eventSamples.size() > 1) {
      ArrayList<DailyRecurrencePattern> weeklyPatterns = findWeeklyPatterns();
      EventSample firstEventSample = eventSamples.get(0);
      EventSample secondEventSample = eventSamples.get(1);
      long timeSpanBetweenFirstTwoSamples = secondEventSample.getTimestamp() - firstEventSample.getTimestamp();
      DailyRecurrencePattern foundRecurrencePattern = new DailyRecurrencePattern(Math.round(timeSpanBetweenFirstTwoSamples / TimeUtils.MILLIS_1_DAY));
      return asList(foundRecurrencePattern);
    }
    return Collections.emptyList();
  }

  private ArrayList<DailyRecurrencePattern> findWeeklyPatterns() {
    WeeklyOccurrancesTable weeklyOccurrancesTable = null;
    return null;
  }

  private class WeeklyOccurrancesTable {}
}
