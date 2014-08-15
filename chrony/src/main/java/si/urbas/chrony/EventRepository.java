package si.urbas.chrony;

public interface EventRepository {
  void addEvent(EventSample eventSample);
  Iterable<EventSample> allEvents();
}
