package si.urbas.chrony;

import java.util.Set;

public interface EventRepository {
  void addEvent(EventSample eventSample);
  Set<String> allEvents();
}
