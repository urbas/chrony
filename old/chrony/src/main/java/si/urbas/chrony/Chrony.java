package si.urbas.chrony;

import java.util.Set;

public class Chrony implements EventRepository {

  private final EventRepository eventRepository;

  public Chrony(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  @Override
  public void addEvent(Event event) {
    eventRepository.addEvent(event);
  }

  @Override
  public Set<String> allEvents() {
    return eventRepository.allEvents();
  }
}
