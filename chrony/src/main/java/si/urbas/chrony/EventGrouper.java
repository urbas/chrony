package si.urbas.chrony;

public interface EventGrouper {
  Iterable<Event> extractEventGroups(EventRepository eventRepository);
}
