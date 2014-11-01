package si.urbas.chrony.util;

import si.urbas.chrony.EventSample;

import java.util.Iterator;

public class EventSampleAssertions {

  public static void assertEventSamplesOrdered(Iterable<EventSample> eventSamples) {
    Iterator<EventSample> eventSampleIterator = eventSamples.iterator();
    if (eventSampleIterator.hasNext()) {
      EventSample previousEventSample = eventSampleIterator.next();
      while (eventSampleIterator.hasNext()) {
        EventSample currentEventSample = eventSampleIterator.next();
        if (currentEventSample.getTimestampInMillis() < previousEventSample.getTimestampInMillis()) {
          throw new IllegalArgumentException("The list of event samples is not ordered by ascending timestamp.");
        }
        previousEventSample = currentEventSample;
      }
    }
  }

}
