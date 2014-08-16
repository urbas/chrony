package si.urbas.chrony;

import java.util.Set;

public class Chrony {

  private final EventRepository eventRepository;

  public Chrony(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  public void addEvent(Event event) {
    eventRepository.addEvent(event);
  }

  public Set<String> allEvents() {
    return eventRepository.allEvents();
  }
}
