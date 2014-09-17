package si.urbas.chrony.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.util.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static si.urbas.chrony.util.EventSampleAssertions.assertEventSamplesOrdered;

public class DayRecurrenceAnalyser {

  private static final int MIN_RECURRENCE_THRESHOLD = 2;
  private static final int WEEKLY_RECURRENCE_PERIOD = 7;
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
      ArrayList<DailyRecurrencePattern> foundPatterns = findWeeklyPatterns();
      System.out.println("Test!");
      for (DailyRecurrencePattern weeklyPattern : foundPatterns) {
        System.out.println("Weekly pattern: " + weeklyPattern);
      }
      findSubWeekPatterns(foundPatterns);
      return foundPatterns;
    }
    return Collections.emptyList();
  }

  private void findSubWeekPatterns(ArrayList<DailyRecurrencePattern> foundPatterns) {
    EventSample firstEventSample = eventSamples.get(0);
    EventSample secondEventSample = eventSamples.get(1);
    long timeSpanBetweenFirstTwoSamples = secondEventSample.getTimestamp() - firstEventSample.getTimestamp();
    if (timeSpanBetweenFirstTwoSamples < TimeUtils.MILLIS_1_WEEK) {
      DailyRecurrencePattern foundRecurrencePattern = new DailyRecurrencePattern(Math.round(timeSpanBetweenFirstTwoSamples / TimeUtils.MILLIS_1_DAY));
      foundPatterns.add(foundRecurrencePattern);
    }
  }

  private ArrayList<DailyRecurrencePattern> findWeeklyPatterns() {
    ArrayList<DailyRecurrencePattern> dailyRecurrencePatterns = new ArrayList<DailyRecurrencePattern>();
    WeeklyOccurrancesTable weeklyOccurrancesTable = new WeeklyOccurrancesTable(eventSamples);
    for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
      if (weeklyOccurrancesTable.getOccurrancesOnDay(dayIndex) >= MIN_RECURRENCE_THRESHOLD) {
        dailyRecurrencePatterns.add(new DailyRecurrencePattern(WEEKLY_RECURRENCE_PERIOD));
      }
    }
    return dailyRecurrencePatterns;
  }

  private static class WeeklyOccurrancesTable {

    private final int[] dayOfWeekOccurrances = new int[7];

    public WeeklyOccurrancesTable(List<EventSample> eventSamples) {
      extractWeeklyOccurrances(eventSamples);
    }

    public int getOccurrancesOnDay(int i) {
      return dayOfWeekOccurrances[i];
    }

    private void extractWeeklyOccurrances(List<EventSample> eventSamples) {
      Calendar calendar = Calendar.getInstance();
      for (EventSample eventSample : eventSamples) {
        calendar.setTimeInMillis(eventSample.getTimestamp());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        dayOfWeekOccurrances[dayOfWeek - 1]++;
      }
    }
  }
}
