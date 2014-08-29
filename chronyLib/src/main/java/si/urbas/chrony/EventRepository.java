package si.urbas.chrony;

import si.urbas.chrony.util.ChangeListener;

import java.util.List;

public interface EventRepository {
  void addEvent(Event event);
  List<String> allEvents();
  List<Long> timestampsOf(String eventName);
  void removeTimestamp(String eventName, Long timestamp);
  void clear();
  void registerChangeListener(ChangeListener changeListener);
}
