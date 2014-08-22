package si.urbas.chrony;

import java.util.ArrayList;
import java.util.Collections;
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
    ArrayList<AnalysedEvent> analysedEvents = new ArrayList<AnalysedEvent>();
    for (String eventName : eventRepository.allEvents()) {
      List<Long> eventTimestamps = eventRepository.timestampsOf(eventName);
      analysedEvents.add(new SimpleAnalysedEvent(eventName, eventTimestamps.size()));
    }
    return analysedEvents;
  }
}
