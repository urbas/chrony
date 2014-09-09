package si.urbas.chrony;

import java.util.List;

public interface EventRepository {

  void addEvent(Event event);

  Event getEvent(String eventName);

  void addEventSample(EventSample eventSample);

  List<Event> allEvents();

  List<EventSample> samplesOf(String eventName);

  void removeEventSample(String eventName, Long timestamp);

  void clear();
}
