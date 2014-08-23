package si.urbas.chrony.analysis;

import si.urbas.chrony.EventRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventTimeMetrics {

  public final String name;
  public final int count;
  public final long newestTimestamp;
  public final long oldestTimestamp;

  public EventTimeMetrics(String name, int count, long newestTimestamp, long oldestTimestamp) {
    this.name = name;
    this.count = count;
    this.newestTimestamp = newestTimestamp;
    this.oldestTimestamp = oldestTimestamp;
  }

  public static ArrayList<EventTimeMetrics> calculateMetrics(EventRepository eventRepository) {
    ArrayList<EventTimeMetrics> analysedEvents = new ArrayList<EventTimeMetrics>();
    for (String eventName : eventRepository.allEvents()) {
      analysedEvents.add(calculateStatistics(eventName, eventRepository.timestampsOf(eventName)));
    }
    return analysedEvents;
  }

  private static EventTimeMetrics calculateStatistics(String eventName, List<Long> eventTimestamps) {
    long latestTimestampForEvent = Collections.max(eventTimestamps);
    long oldestTimestampForEvent = Collections.min(eventTimestamps);
    return new EventTimeMetrics(eventName, eventTimestamps.size(), latestTimestampForEvent, oldestTimestampForEvent);
  }

}
