package si.urbas.chrony.analysis;

import si.urbas.chrony.AnalysedEvent;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.Analysis;
import si.urbas.chrony.metrics.EventTemporalMetrics;

import java.util.*;

import static java.util.Collections.emptyList;

class SimpleAnalysis implements Analysis {
  private final List<AnalysedEvent> analysedEvents;

  SimpleAnalysis(EventRepository eventRepository) {
    analysedEvents = Collections.unmodifiableList(analyseEvents(eventRepository));
  }

  @Override
  public List<AnalysedEvent> getAnalysedEvents() {
    return analysedEvents;
  }

  private static List<AnalysedEvent> analyseEvents(EventRepository eventRepository) {
    ArrayList<EventTemporalMetrics> eventTemporalMetrics = EventTemporalMetrics.calculate(eventRepository);
    if (eventTemporalMetrics.size() == 0) {
      return emptyList();
    } else {
      return analyseEvents(eventTemporalMetrics);
    }
  }

  private static List<AnalysedEvent> analyseEvents(ArrayList<EventTemporalMetrics> perEventMetricsList) {
    ArrayList<AnalysedEvent> analysedEvents = new ArrayList<AnalysedEvent>();
    for (EventTemporalMetrics perEventMetrics : perEventMetricsList) {
      analysedEvents.add(new SimpleAnalysedEvent(perEventMetrics.name, perEventMetrics.count, calculateRelevanceOfEvent(perEventMetrics)));
    }
    Collections.sort(analysedEvents, new MostRelevantAnalysedEventComparator());
    return analysedEvents;
  }

  private static float calculateRelevanceOfEvent(EventTemporalMetrics perEventMetrics) {
    long now = new Date().getTime();
    return -Math.abs(now - perEventMetrics.newestTimestamp);
  }

  private static class MostRelevantAnalysedEventComparator implements Comparator<AnalysedEvent> {
    @Override
    public int compare(AnalysedEvent analysedEventA, AnalysedEvent analysedEventB) {
      return Float.compare(analysedEventB.getRelevance(), analysedEventA.getRelevance());
    }
  }
}
