package si.urbas.chrony;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class MemoryEventRepository implements EventRepository {

  private final ArrayList<Event> events = new ArrayList<>();

  @Override
  public void addEvent(Event event) {
    events.add(event);
  }

  @Override
  public Set<String> allEvents() {
    return events.stream()
                 .map(eventSample -> eventSample.name)
                 .collect(Collectors.toSet());
  }
}
