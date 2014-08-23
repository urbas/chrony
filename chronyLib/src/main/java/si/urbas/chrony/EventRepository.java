package si.urbas.chrony;

import java.util.List;

public interface EventRepository {
  void addEvent(Event event);
  List<String> allEvents();
  List<Long> timestampsOf(String eventName);
  void clear();
}
