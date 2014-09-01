package si.urbas.chrony;

import si.urbas.chrony.util.ChangeListener;

import java.util.List;

public interface EventRepository {
  void addEvent(String eventName);
  void addEventSample(String eventName, long timestamp, Object data);
  List<String> allEvents();
  List<Long> timestampsOf(String eventName);
  void removeEvent(String eventName);
  void removeTimestamp(String eventName, Long timestamp);
  void clear();
  void registerChangeListener(ChangeListener changeListener);
}
