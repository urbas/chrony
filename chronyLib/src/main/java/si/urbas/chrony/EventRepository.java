package si.urbas.chrony;

import si.urbas.chrony.util.ChangeListener;

import java.util.List;

public interface EventRepository {
  void addEvent(Event event);
  void addEventSample(EventSample eventSample);
  List<Event> allEvents();
  List<EventSample> samplesOf(String eventName);
  void removeEventSample(String eventName, Long timestamp);
  void clear();
  void registerChangeListener(ChangeListener changeListener);
}
