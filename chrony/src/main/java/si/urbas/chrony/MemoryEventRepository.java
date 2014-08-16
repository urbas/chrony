package si.urbas.chrony;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class MemoryEventRepository implements EventRepository {

  private final ArrayList<EventSample> eventSamples = new ArrayList<>();

  @Override
  public void addEvent(EventSample eventSample) {
    eventSamples.add(eventSample);
  }

  @Override
  public Set<String> allEvents() {
    return eventSamples.stream()
                       .map(eventSample -> eventSample.name)
                       .collect(Collectors.toSet());
  }
}
