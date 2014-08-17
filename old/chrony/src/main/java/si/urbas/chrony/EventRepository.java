package si.urbas.chrony;

import java.util.Set;

public interface EventRepository {
  void addEvent(Event event);
  Set<String> allEvents();
}
