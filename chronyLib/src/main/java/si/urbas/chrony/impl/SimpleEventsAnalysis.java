package si.urbas.chrony.impl;

import si.urbas.chrony.AnalysedEvent;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.EventsAnalysis;
import si.urbas.chrony.analysis.EventTimeMetrics;
import si.urbas.chrony.analysis.GlobalTimeMetrics;

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
    ArrayList<EventTimeMetrics> eventTimeMetrics = EventTimeMetrics.calculateMetrics(eventRepository);
    GlobalTimeMetrics globalTimeMetrics = GlobalTimeMetrics.calculateMetrics(eventTimeMetrics);
    return createAnalysis(eventTimeMetrics, globalTimeMetrics);
  }

  private static ArrayList<AnalysedEvent> createAnalysis(ArrayList<EventTimeMetrics> perEventMetricsList, GlobalTimeMetrics globalTimeMetrics) {
    ArrayList<AnalysedEvent> analysedEvents = new ArrayList<AnalysedEvent>();
    for (EventTimeMetrics perEventMetrics : perEventMetricsList) {
      analysedEvents.add(new SimpleAnalysedEvent(perEventMetrics.name, perEventMetrics.count, calculateRelevanceOfEvent(perEventMetrics, globalTimeMetrics)));
    }
    Collections.sort(analysedEvents, new MostRelevantAnalysedEventComparator());
    return analysedEvents;
  }

  private static float calculateRelevanceOfEvent(EventTimeMetrics perEventMetrics, GlobalTimeMetrics globalTimeMetrics) {
    long oldestToNewestTimeSpan = globalTimeMetrics.newestTimestamp - globalTimeMetrics.oldestEventRefreshTimestamp;
    long oldestToThisTimeSpan = perEventMetrics.newestTimestamp - globalTimeMetrics.oldestEventRefreshTimestamp;
    return (float) oldestToThisTimeSpan / (float) oldestToNewestTimeSpan;
  }

  private static class MostRelevantAnalysedEventComparator implements Comparator<AnalysedEvent> {
    @Override
    public int compare(AnalysedEvent analysedEventA, AnalysedEvent analysedEventB) {
      return Float.compare(analysedEventB.getRelevance(), analysedEventA.getRelevance());
    }
  }
}
