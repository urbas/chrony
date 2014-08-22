package si.urbas.chrony.impl;

import si.urbas.chrony.AnalysedEvent;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.EventsAnalysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimpleEventsAnalysis implements EventsAnalysis {
  private final List<AnalysedEvent> analysedEvents;

  public SimpleEventsAnalysis(EventRepository eventRepository) {
    analysedEvents = Collections.unmodifiableList(analyseEvents(eventRepository));
  }

  @Override
  public List<AnalysedEvent> getAnalysedEvents() {
    return analysedEvents;
  }

  private static List<AnalysedEvent> analyseEvents(EventRepository eventRepository) {
    ArrayList<PerEventMetrics> perEventMetricsList = collectPerEventMetrics(eventRepository);
    GlobalMetrics globalMetrics = calculateGlobalMetrics(perEventMetricsList);
    return createAnalysis(perEventMetricsList, globalMetrics);
  }

  private static ArrayList<AnalysedEvent> createAnalysis(ArrayList<PerEventMetrics> perEventMetricsList, GlobalMetrics globalMetrics) {
    ArrayList<AnalysedEvent> analysedEvents = new ArrayList<AnalysedEvent>();
    for (PerEventMetrics perEventMetrics : perEventMetricsList) {
      analysedEvents.add(new SimpleAnalysedEvent(perEventMetrics.name, perEventMetrics.count, calculateRelevanceOfEvent(perEventMetrics, globalMetrics)));
    }
    Collections.sort(analysedEvents, new MostRelevantAnalysedEventComparator());
    return analysedEvents;
  }

  private static float calculateRelevanceOfEvent(PerEventMetrics perEventMetrics, GlobalMetrics globalMetrics) {
    long oldestToNewestTimeSpan = globalMetrics.newestTimestamp - globalMetrics.oldestEventRefreshTimestamp;
    long oldestToThisTimeSpan = perEventMetrics.newestTimestamp - globalMetrics.oldestEventRefreshTimestamp;
    return (float) oldestToThisTimeSpan / (float) oldestToNewestTimeSpan;
  }

  private static GlobalMetrics calculateGlobalMetrics(ArrayList<PerEventMetrics> perEventMetricsList) {
    long oldestTimestamp = Long.MAX_VALUE;
    long newestTimestamp = Long.MIN_VALUE;
    long oldestEventRefreshTimestamp = Long.MAX_VALUE;
    for (PerEventMetrics perEventMetrics : perEventMetricsList) {
      if (perEventMetrics.newestTimestamp > newestTimestamp) {
        newestTimestamp = perEventMetrics.newestTimestamp;
      }
      if (perEventMetrics.oldestTimestamp < oldestTimestamp) {
        oldestTimestamp = perEventMetrics.oldestTimestamp;
      }
      if (perEventMetrics.newestTimestamp < oldestEventRefreshTimestamp) {
        oldestEventRefreshTimestamp = perEventMetrics.newestTimestamp;
      }
    }
    return new GlobalMetrics(oldestTimestamp, newestTimestamp, oldestEventRefreshTimestamp);
  }

  private static ArrayList<PerEventMetrics> collectPerEventMetrics(EventRepository eventRepository) {
    ArrayList<PerEventMetrics> analysedEvents = new ArrayList<PerEventMetrics>();
    for (String eventName : eventRepository.allEvents()) {
      List<Long> eventTimestamps = eventRepository.timestampsOf(eventName);
      long latestTimestampForEvent = Collections.max(eventTimestamps);
      long oldestTimestampForEvent = Collections.min(eventTimestamps);
      analysedEvents.add(new PerEventMetrics(eventName, eventTimestamps.size(), latestTimestampForEvent, oldestTimestampForEvent));
    }
    return analysedEvents;
  }

  private static class MostRelevantAnalysedEventComparator implements Comparator<AnalysedEvent> {
    @Override
    public int compare(AnalysedEvent analysedEventA, AnalysedEvent analysedEventB) {
      return Float.compare(analysedEventB.getRelevance(), analysedEventA.getRelevance());
    }
  }
}
