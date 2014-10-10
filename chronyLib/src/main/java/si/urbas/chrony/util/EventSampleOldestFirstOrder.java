package si.urbas.chrony.util;

import si.urbas.chrony.EventSample;

import java.util.Comparator;

public class EventSampleOldestFirstOrder implements Comparator<EventSample> {

  public static final EventSampleOldestFirstOrder INSTANCE = new EventSampleOldestFirstOrder();

  private EventSampleOldestFirstOrder() {}

  @Override
  public int compare(EventSample eventSample1, EventSample eventSample2) {
    return Long.compare(eventSample1.getTimestamp(), eventSample2.getTimestamp());
  }
}
