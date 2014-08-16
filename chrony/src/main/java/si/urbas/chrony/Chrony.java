package si.urbas.chrony;

import java.util.Set;

public class Chrony {

  private final EventRepository eventRepository;

  public Chrony(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  public void addEvent(EventSample eventSample) {
    eventRepository.addEvent(eventSample);
  }

  public Set<String> allEvents() {
    return eventRepository.allEvents();
  }
}
