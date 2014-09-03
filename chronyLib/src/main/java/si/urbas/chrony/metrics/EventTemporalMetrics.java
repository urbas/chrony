package si.urbas.chrony.metrics;

import si.urbas.chrony.Event;
import si.urbas.chrony.EventRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventTemporalMetrics {

  public final int count;
  public final long newestTimestamp;
  public final long oldestTimestamp;
  public final Event event;

  public EventTemporalMetrics(Event event, int count, long newestTimestamp, long oldestTimestamp) {
    this.event = event;
    this.count = count;
    this.newestTimestamp = newestTimestamp;
    this.oldestTimestamp = oldestTimestamp;
  }

  public static ArrayList<EventTemporalMetrics> calculate(EventRepository eventRepository) {
    ArrayList<EventTemporalMetrics> analysedEvents = new ArrayList<EventTemporalMetrics>();
    for (Event event : eventRepository.allEvents()) {
      analysedEvents.add(calculateStatistics(event, eventRepository.timestampsOf(event.getEventName())));
    }
    return analysedEvents;
  }

  private static EventTemporalMetrics calculateStatistics(Event event, List<Long> eventTimestamps) {
    long latestTimestampForEvent = eventTimestamps.size() == 0 ? Long.MIN_VALUE : Collections.max(eventTimestamps);
    long oldestTimestampForEvent = eventTimestamps.size() == 0 ? Long.MIN_VALUE : Collections.min(eventTimestamps);
    return new EventTemporalMetrics(event, eventTimestamps.size(), latestTimestampForEvent, oldestTimestampForEvent);
  }

}
