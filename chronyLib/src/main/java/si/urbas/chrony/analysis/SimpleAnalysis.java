package si.urbas.chrony.analysis;

import si.urbas.chrony.AnalysedEvent;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.Analysis;
import si.urbas.chrony.metrics.EventTemporalMetrics;
import si.urbas.chrony.metrics.GlobalTemporalMetrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Collections.emptyList;
import static si.urbas.chrony.metrics.GlobalTemporalMetrics.calculate;

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
      return createAnalysis(eventTemporalMetrics, calculate(eventTemporalMetrics));
    }
  }

  private static List<AnalysedEvent> createAnalysis(ArrayList<EventTemporalMetrics> perEventMetricsList, GlobalTemporalMetrics globalTemporalMetrics) {
    ArrayList<AnalysedEvent> analysedEvents = new ArrayList<AnalysedEvent>();
    for (EventTemporalMetrics perEventMetrics : perEventMetricsList) {
      analysedEvents.add(new SimpleAnalysedEvent(perEventMetrics.name, perEventMetrics.count, calculateRelevanceOfEvent(perEventMetrics, globalTemporalMetrics)));
    }
    Collections.sort(analysedEvents, new MostRelevantAnalysedEventComparator());
    return analysedEvents;
  }

  private static float calculateRelevanceOfEvent(EventTemporalMetrics perEventMetrics, GlobalTemporalMetrics globalTemporalMetrics) {
    long oldestToNewestTimeSpan = globalTemporalMetrics.newestTimestamp - globalTemporalMetrics.oldestEventRefreshTimestamp;
    long oldestToThisTimeSpan = perEventMetrics.newestTimestamp - globalTemporalMetrics.oldestEventRefreshTimestamp;
    return (float) oldestToThisTimeSpan / (float) oldestToNewestTimeSpan;
  }

  private static class MostRelevantAnalysedEventComparator implements Comparator<AnalysedEvent> {
    @Override
    public int compare(AnalysedEvent analysedEventA, AnalysedEvent analysedEventB) {
      return Float.compare(analysedEventB.getRelevance(), analysedEventA.getRelevance());
    }
  }
}
