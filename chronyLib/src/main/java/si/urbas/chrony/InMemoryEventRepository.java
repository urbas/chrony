package si.urbas.chrony;

import si.urbas.chrony.impl.EventTimestamps;

import java.util.Collections;
import java.util.HashMap;

public class InMemoryEventRepository implements EventRepository {

  private HashMap<String, EventTimestamps> eventNameToEventStoreMap = new HashMap<String, EventTimestamps>();

  @Override
  public void addEvent(Event event) {
    EventTimestamps eventTimestamps = eventNameToEventStoreMap.get(event.name);
    if (eventTimestamps == null) {
      eventTimestamps = new EventTimestamps();
      eventNameToEventStoreMap.put(event.name, eventTimestamps);
    }
    eventTimestamps.addTimestamp(event.timestamp);
  }

  @Override
  public Iterable<String> allEvents() {
    return Collections.unmodifiableSet(eventNameToEventStoreMap.keySet());
  }

  @Override
  public int eventCount(String eventName) {
    EventTimestamps eventTimestamps = eventNameToEventStoreMap.get(eventName);
    return eventTimestamps == null ? 0 : eventTimestamps.size();
  }
}
