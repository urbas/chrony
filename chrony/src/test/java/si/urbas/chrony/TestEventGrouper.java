package si.urbas.chrony;

import java.util.ArrayList;

class TestEventGrouper implements EventGrouper {
  @Override
  public Iterable<Event> extractEventGroups(EventRepository eventRepository) {
    ArrayList<Event> events = new ArrayList<>();
    for (EventSample eventSample : eventRepository.allEvents()) {
      events.add(new Event(eventSample.name));
    }
    return events;
  }
}
