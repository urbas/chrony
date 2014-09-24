package si.urbas.chrony.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.metrics.EventTemporalMetrics;
import si.urbas.chrony.recurrence.DailyPeriodRecurrence;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.util.TimeUtils;

import java.util.*;

import static si.urbas.chrony.util.EventSampleAssertions.assertEventSamplesOrdered;

public class DayRecurrenceAnalyser implements RecurrenceAnalyser {

  private static final double MIN_TIME_SPAN_PERCENTAGE = 0.75;
  private static final int WEEKLY_RECURRENCE_PERIOD = 7;
  private static final int MIN_EVENT_SAMPLES_FOR_RECURRENCE = 2;
  private static final int HOURS_IN_A_DAY = 24;
  private final List<Recurrence> foundPatterns;
  private EventTemporalMetrics eventTemporalMetrics;

  /**
   * @param eventSamples this list must be ordered by the timestamp of the event samples (increasing order).
   */
  public DayRecurrenceAnalyser(List<EventSample> eventSamples) {
    this(eventSamples, EventTemporalMetrics.calculate(eventSamples));
  }

  private DayRecurrenceAnalyser(List<EventSample> eventSamples, EventTemporalMetrics temporalMetrics) {
    assertEventSamplesOrdered(eventSamples);
    this.eventTemporalMetrics = temporalMetrics;
    this.foundPatterns = findPatterns(new ArrayList<EventSample>(eventSamples));
  }

  private List<Recurrence> findPatterns(ArrayList<EventSample> eventSamples) {
    if (eventSamples.size() >= MIN_EVENT_SAMPLES_FOR_RECURRENCE && isEventTimeSpanNonZero()) {
      ArrayList<Recurrence> foundPatterns = new ArrayList<Recurrence>();
      findWeeklyPatterns(eventSamples, foundPatterns);
      findSubWeekPatterns(eventSamples, foundPatterns);
      return foundPatterns;
    }
    return Collections.emptyList();
  }

  @Override
  public List<Recurrence> foundRecurrences() {
    return foundPatterns;
  }

  private boolean isEventTimeSpanNonZero() {
    return eventTemporalMetrics.newestTimestamp - eventTemporalMetrics.oldestTimestamp > 0;
  }

  private void findWeeklyPatterns(ArrayList<EventSample> eventSamples, ArrayList<Recurrence> foundPatterns) {
    WeeklyOccurrencesTable weeklyOccurrencesTable = new WeeklyOccurrencesTable(eventSamples);
    for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
      Collection<EventSample> eventSamplesOfWeeklyPattern = weeklyOccurrencesTable.getEventSamplesOfWeeklyPattern(dayIndex);
      if (eventSamplesOfWeeklyPattern.size() >= MIN_EVENT_SAMPLES_FOR_RECURRENCE) {
        for (HourBasedRecurrenceCluster hourBasedRecurrenceCluster : hourlyClusters(eventSamplesOfWeeklyPattern)) {
          if (isRecurrenceConfident(hourBasedRecurrenceCluster.getEventSamplesInCluster())) {
            foundPatterns.add(new DailyPeriodRecurrence(WEEKLY_RECURRENCE_PERIOD, 0, 0));
            eventSamples.removeAll(hourBasedRecurrenceCluster.getEventSamplesInCluster());
          }
        }
      }
    }
  }

  private boolean isRecurrenceConfident(List<EventSample> samplesWithinRecurrencePattern) {
    boolean hasMinimumNumberOfOccurrences = samplesWithinRecurrencePattern.size() >= MIN_EVENT_SAMPLES_FOR_RECURRENCE;
    return hasMinimumNumberOfOccurrences && hasLongEnoughTimeSpan(samplesWithinRecurrencePattern);
  }

  private boolean hasLongEnoughTimeSpan(List<EventSample> hourBasedRecurrenceCluster) {
    return (double) timeSpanOfCluster(hourBasedRecurrenceCluster) / (double) getTimeSpanOfEvent() > MIN_TIME_SPAN_PERCENTAGE;
  }

  private long getTimeSpanOfEvent() {return eventTemporalMetrics.newestTimestamp - eventTemporalMetrics.oldestTimestamp;}

  private long timeSpanOfCluster(List<EventSample> hourBasedRecurrenceCluster) {
    EventSample lastSample = hourBasedRecurrenceCluster.get(hourBasedRecurrenceCluster.size() - 1);
    EventSample firstSample = hourBasedRecurrenceCluster.get(0);
    return lastSample.getTimestamp() - firstSample.getTimestamp();
  }

  private void findSubWeekPatterns(ArrayList<EventSample> eventSamples, ArrayList<Recurrence> foundPatterns) {
    if (eventSamples.size() >= MIN_EVENT_SAMPLES_FOR_RECURRENCE) {
      EventSample firstEventSample = eventSamples.get(0);
      EventSample secondEventSample = eventSamples.get(1);
      long timeSpanBetweenFirstTwoSamples = secondEventSample.getTimestamp() - firstEventSample.getTimestamp();
      if (timeSpanBetweenFirstTwoSamples < TimeUtils.WEEK_IN_MILLIS) {
        DailyPeriodRecurrence foundRecurrencePattern = new DailyPeriodRecurrence(Math.round(timeSpanBetweenFirstTwoSamples / TimeUtils.DAY_IN_MILLIS), 0, 0);
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

  private ArrayList<HourBasedRecurrenceCluster> hourlyClusters(Collection<EventSample> eventSamplesOfWeeklyPattern) {
    ArrayList<HourBasedRecurrenceCluster> hourlyClusters = new ArrayList<HourBasedRecurrenceCluster>(HOURS_IN_A_DAY);
    for (int hourOfDay = 0; hourOfDay < HOURS_IN_A_DAY; hourOfDay++) {
      hourlyClusters.add(new HourBasedRecurrenceCluster(hourOfDay, eventSamplesOfWeeklyPattern));
    }
    return hourlyClusters;
  }

  private static class HourBasedRecurrenceCluster {

    private final ArrayList<EventSample> eventSamplesInCluster;

    public HourBasedRecurrenceCluster(int hourOfDay, Collection<EventSample> eventSamplesOfWeeklyPattern) {
      this.eventSamplesInCluster = new ArrayList<EventSample>();
      Calendar calendar = Calendar.getInstance();
      for (EventSample eventSample : eventSamplesOfWeeklyPattern) {
        calendar.setTimeInMillis(eventSample.getTimestamp());
        int hourOfSample = calendar.get(Calendar.HOUR_OF_DAY) - 1;
        if (hourOfSample >= hourOfDay && hourOfSample < hourOfDay + 1) {
          this.eventSamplesInCluster.add(eventSample);
        }
      }
    }

    public List<EventSample> getEventSamplesInCluster() {
      return eventSamplesInCluster;
    }
  }
}
