package si.urbas.chrony.util;

import si.urbas.chrony.EventSample;

import java.util.Iterator;
import java.util.List;

public class EventSampleAssertions {
  public static void assertEventSamplesOrdered(List<EventSample> eventSamples) {
    if (eventSamples.size() > 1) {
      Iterator<EventSample> eventSampleIterator = eventSamples.iterator();
      EventSample previousEventSample = eventSampleIterator.next();
      while (eventSampleIterator.hasNext()) {
        EventSample currentEventSample = eventSampleIterator.next();
        if (currentEventSample.getTimestamp() < previousEventSample.getTimestamp()) {
          throw new IllegalArgumentException("The list of event samples is not ordered by increasing timestamp.");
        }
        previousEventSample = currentEventSample;
      }
    }
  }
}
