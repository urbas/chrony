package si.urbas.chrony.analysis;

import si.urbas.chrony.Event;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.metrics.EventTemporalMetrics;
import si.urbas.chrony.util.TimeUtils;

import java.util.*;

import static si.urbas.chrony.util.EventSampleAssertions.assertEventSamplesOrdered;

public class DayRecurrenceAnalyser {

  private static final int MIN_RECURRENCE_THRESHOLD = 2;
  private static final double MIN_TIME_SPAN_PERCENTAGE = 0.75;
  private static final int WEEKLY_RECURRENCE_PERIOD = 7;
  private static final int MIN_EVENT_SAMPLES_FOR_RECURRENCE = 2;
  private final List<DailyRecurrencePattern> foundPatterns;
  private EventTemporalMetrics eventTemporalMetrics;

  /**
   * @param eventSamples this list must be ordered by the timestamp of the event samples (increasing order).
   */
  public DayRecurrenceAnalyser(Event event, List<EventSample> eventSamples) {
    this(eventSamples, EventTemporalMetrics.calculate(event, eventSamples));
  }

  private DayRecurrenceAnalyser(List<EventSample> eventSamples, EventTemporalMetrics temporalMetrics) {
    assertEventSamplesOrdered(eventSamples);
    this.eventTemporalMetrics = temporalMetrics;
    this.foundPatterns = findPatterns(new ArrayList<EventSample>(eventSamples));
  }

  private List<DailyRecurrencePattern> findPatterns(ArrayList<EventSample> eventSamples) {
    if (eventSamples.size() >= MIN_EVENT_SAMPLES_FOR_RECURRENCE && isEventTimeSpanNonZero()) {
      ArrayList<DailyRecurrencePattern> foundPatterns = new ArrayList<DailyRecurrencePattern>();
      findWeeklyPatterns(eventSamples, foundPatterns);
      findSubWeekPatterns(eventSamples, foundPatterns);
      return foundPatterns;
    }
    return Collections.emptyList();

  }

  public List<DailyRecurrencePattern> foundPatterns() {
    return foundPatterns;
  }

  private boolean isEventTimeSpanNonZero() {
    return eventTemporalMetrics.newestTimestamp - eventTemporalMetrics.oldestTimestamp > 0;
  }

  private void findWeeklyPatterns(ArrayList<EventSample> eventSamples, ArrayList<DailyRecurrencePattern> foundPatterns) {
    WeeklyOccurrencesTable weeklyOccurrencesTable = new WeeklyOccurrencesTable(eventSamples);
    for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
      HourBasedRecurrenceClustering hourBasedRecurrenceClustering = new HourBasedRecurrenceClustering(weeklyOccurrencesTable.getEventSamplesOfWeeklyPattern(dayIndex));
      for (HourBasedRecurrenceCluster hourBasedRecurrenceCluster : hourBasedRecurrenceClustering.hourlyClusters()) {
        if (isConfident(hourBasedRecurrenceCluster.getEventSamplesInCluster())) {
          foundPatterns.add(new DailyRecurrencePattern(WEEKLY_RECURRENCE_PERIOD));
          eventSamples.removeAll(hourBasedRecurrenceCluster.getEventSamplesInCluster());
        }
      }
    }
  }

  private boolean isConfident(List<EventSample> hourBasedRecurrenceCluster) {
    boolean hasMinimumOccurrences = hourBasedRecurrenceCluster.size() >= MIN_EVENT_SAMPLES_FOR_RECURRENCE;
    return hasMinimumOccurrences && hasLongEnoughTimeSpan(hourBasedRecurrenceCluster);
  }

  private boolean hasLongEnoughTimeSpan(List<EventSample> hourBasedRecurrenceCluster) {
    return (double) timeSpanOfCluster(hourBasedRecurrenceCluster) / (double)getTimeSpanOfEvent() > MIN_TIME_SPAN_PERCENTAGE;
  }

  private long getTimeSpanOfEvent() {return eventTemporalMetrics.newestTimestamp - eventTemporalMetrics.oldestTimestamp;}

  private long timeSpanOfCluster(List<EventSample> hourBasedRecurrenceCluster) {
    EventSample lastSample = hourBasedRecurrenceCluster.get(hourBasedRecurrenceCluster.size() - 1);
    EventSample firstSample = hourBasedRecurrenceCluster.get(0);
    return lastSample.getTimestamp() - firstSample.getTimestamp();
  }

  private void findSubWeekPatterns(ArrayList<EventSample> eventSamples, ArrayList<DailyRecurrencePattern> foundPatterns) {
    if (eventSamples.size() >= MIN_EVENT_SAMPLES_FOR_RECURRENCE) {
      EventSample firstEventSample = eventSamples.get(0);
      EventSample secondEventSample = eventSamples.get(1);
      long timeSpanBetweenFirstTwoSamples = secondEventSample.getTimestamp() - firstEventSample.getTimestamp();
      if (timeSpanBetweenFirstTwoSamples < TimeUtils.MILLIS_1_WEEK) {
        DailyRecurrencePattern foundRecurrencePattern = new DailyRecurrencePattern(Math.round(timeSpanBetweenFirstTwoSamples / TimeUtils.MILLIS_1_DAY));
        foundPatterns.add(foundRecurrencePattern);
      }
    }
  }

  private static class WeeklyOccurrencesTable {

    private final ArrayList<ArrayList<EventSample>> weeklyOccurrencesStore;

    public WeeklyOccurrencesTable(List<EventSample> eventSamples) {
      weeklyOccurrencesStore = extractWeeklyOccurrences(eventSamples);
    }

    public Collection<EventSample> getEventSamplesOfWeeklyPattern(int dayIndex) {
      return weeklyOccurrencesStore.get(dayIndex);
    }

    private static ArrayList<ArrayList<EventSample>> extractWeeklyOccurrences(List<EventSample> eventSamples) {
      ArrayList<ArrayList<EventSample>> weeklyOccurrencesStore = createWeeklyOccurrencesStore();
      Calendar calendar = Calendar.getInstance();
      for (EventSample eventSample : eventSamples) {
        calendar.setTimeInMillis(eventSample.getTimestamp());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        weeklyOccurrencesStore.get(dayOfWeek - 1).add(eventSample);
      }
      return weeklyOccurrencesStore;
    }

    private static ArrayList<ArrayList<EventSample>> createWeeklyOccurrencesStore() {
      ArrayList<ArrayList<EventSample>> weeklyOccurrencesStore = new ArrayList<ArrayList<EventSample>>(7);
      for (int i = 0; i < 7; i++) {
        weeklyOccurrencesStore.add(new ArrayList<EventSample>());
      }
      return weeklyOccurrencesStore;
    }
  }

  private static class HourBasedRecurrenceClustering {

    private static final int HOURS_IN_A_DAY = 24;
    private final ArrayList<HourBasedRecurrenceCluster> hourlyClusters;

    public HourBasedRecurrenceClustering(Collection<EventSample> eventSamples) {
      this.hourlyClusters = findHourlyClusters(eventSamples);
    }

    public ArrayList<HourBasedRecurrenceCluster> hourlyClusters() {
      return hourlyClusters;
    }

    private ArrayList<HourBasedRecurrenceCluster> findHourlyClusters(Collection<EventSample> eventSamplesOfWeeklyPattern) {
      ArrayList<HourBasedRecurrenceCluster> hourlyClusters = new ArrayList<HourBasedRecurrenceCluster>(HOURS_IN_A_DAY);
      for (int hourOfDay = 0; hourOfDay < HOURS_IN_A_DAY; hourOfDay++) {
        hourlyClusters.add(new HourBasedRecurrenceCluster(hourOfDay, eventSamplesOfWeeklyPattern));
      }
      return hourlyClusters;
    }
  }

  private static class HourBasedRecurrenceCluster {
    private final ArrayList<EventSample> eventSamplesInCluster;

    public HourBasedRecurrenceCluster(int hourOfDay, Collection<EventSample> eventSamplesOfWeeklyPattern) {
      this.eventSamplesInCluster = new ArrayList<EventSample>();
      Calendar calendar = Calendar.getInstance();
      for (EventSample eventSample : eventSamplesOfWeeklyPattern) {
        calendar.setTimeInMillis(eventSample.getTimestamp());
        int hourOfSample = calendar.get(Calendar.HOUR_OF_DAY);
        if (hourOfSample >= hourOfDay && hourOfSample < hourOfDay) {
          this.eventSamplesInCluster.add(eventSample);
        }
      }
    }

    public List<EventSample> getEventSamplesInCluster() {
      return eventSamplesInCluster;
    }
  }
}
