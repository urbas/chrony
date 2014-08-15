package si.urbas.chrony;

import java.util.ArrayList;
import java.util.Collections;

public class MemoryEventRepository implements EventRepository {

  private final ArrayList<EventSample> eventSamples = new ArrayList<>();

  @Override
  public void addEvent(EventSample eventSample) {
    eventSamples.add(eventSample);
  }

  @Override
  public Iterable<EventSample> allEvents() {
    return Collections.unmodifiableList(eventSamples);
  }
}
