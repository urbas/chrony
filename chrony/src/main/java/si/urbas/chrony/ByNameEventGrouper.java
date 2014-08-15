package si.urbas.chrony;

import java.util.HashSet;
import java.util.Set;

class ByNameEventGrouper implements EventGrouper {
  @Override
  public Iterable<Event> extractEventGroups(EventRepository eventRepository) {
    Set<Event> events = new HashSet<>();
    for (EventSample eventSample : eventRepository.allEvents()) {
      events.add(new Event(eventSample.name));
    }
    return events;
  }
}
