package si.urbas.chrony;

public interface EventRepository {
  void addEvent(Event event);
  Iterable<String> allEvents();
  int eventCount(String eventName);
}
