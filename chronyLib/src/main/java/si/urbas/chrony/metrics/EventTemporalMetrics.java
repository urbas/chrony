package si.urbas.chrony.metrics;

import si.urbas.chrony.Event;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.util.EventSampleUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventTemporalMetrics {

  public final int count;
  public final long newestTimestamp;
  public final long oldestTimestamp;

  public EventTemporalMetrics(int count, long newestTimestamp, long oldestTimestamp) {
    this.count = count;
    this.newestTimestamp = newestTimestamp;
    this.oldestTimestamp = oldestTimestamp;
  }

  public static Map<Event, EventTemporalMetrics> calculate(EventRepository eventRepository) {
    Map<Event, EventTemporalMetrics> analysedEvents = new HashMap<Event, EventTemporalMetrics>();
    for (Event event : eventRepository.allEvents()) {
      analysedEvents.put(event, calculate(eventRepository.samplesOf(event.getEventName())));
    }
    return analysedEvents;
  }

  public static EventTemporalMetrics calculate(List<EventSample> eventSamples) {
    long latestTimestampForEvent = eventSamples.size() == 0 ? Long.MIN_VALUE : EventSampleUtils.newestTimestamp(eventSamples);
    long oldestTimestampForEvent = eventSamples.size() == 0 ? Long.MIN_VALUE : EventSampleUtils.oldestTimestamp(eventSamples);
    return new EventTemporalMetrics(eventSamples.size(), latestTimestampForEvent, oldestTimestampForEvent);
  }

  public long entireTimeSpan() {
    return newestTimestamp - oldestTimestamp;
  }
}
