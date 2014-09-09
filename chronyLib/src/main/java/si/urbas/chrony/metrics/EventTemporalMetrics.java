package si.urbas.chrony.metrics;

import si.urbas.chrony.Event;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.util.EventSampleUtils;

import java.util.ArrayList;
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
      analysedEvents.add(calculateStatistics(event, eventRepository.samplesOf(event.getEventName())));
    }
    return analysedEvents;
  }

  private static EventTemporalMetrics calculateStatistics(Event event, List<EventSample> eventSamples) {
    long latestTimestampForEvent = eventSamples.size() == 0 ? Long.MIN_VALUE : EventSampleUtils.getMaximumTimestamp(eventSamples);
    long oldestTimestampForEvent = eventSamples.size() == 0 ? Long.MIN_VALUE : EventSampleUtils.getMinimumTimestamp(eventSamples);
    return new EventTemporalMetrics(event, eventSamples.size(), latestTimestampForEvent, oldestTimestampForEvent);
  }

}
