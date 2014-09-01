package si.urbas.chrony.metrics;

import si.urbas.chrony.EventRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventTemporalMetrics {

  public final String name;
  public final int count;
  public final long newestTimestamp;
  public final long oldestTimestamp;

  public EventTemporalMetrics(String name, int count, long newestTimestamp, long oldestTimestamp) {
    this.name = name;
    this.count = count;
    this.newestTimestamp = newestTimestamp;
    this.oldestTimestamp = oldestTimestamp;
  }

  public static ArrayList<EventTemporalMetrics> calculate(EventRepository eventRepository) {
    ArrayList<EventTemporalMetrics> analysedEvents = new ArrayList<EventTemporalMetrics>();
    for (String eventName : eventRepository.allEvents()) {
      analysedEvents.add(calculateStatistics(eventName, eventRepository.timestampsOf(eventName)));
    }
    return analysedEvents;
  }

  private static EventTemporalMetrics calculateStatistics(String eventName, List<Long> eventTimestamps) {
    long latestTimestampForEvent = eventTimestamps.size() == 0 ? Long.MIN_VALUE : Collections.max(eventTimestamps);
    long oldestTimestampForEvent = eventTimestamps.size() == 0 ? Long.MIN_VALUE : Collections.min(eventTimestamps);
    return new EventTemporalMetrics(eventName, eventTimestamps.size(), latestTimestampForEvent, oldestTimestampForEvent);
  }

}
