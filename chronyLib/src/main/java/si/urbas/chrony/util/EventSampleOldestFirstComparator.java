package si.urbas.chrony.util;

import si.urbas.chrony.EventSample;

import java.util.Comparator;

public class EventSampleOldestFirstComparator implements Comparator<EventSample> {

  public static final EventSampleOldestFirstComparator INSTANCE = new EventSampleOldestFirstComparator();

  @Override
  public int compare(EventSample eventSampleA, EventSample eventSampleB) {
    return eventSampleA.getTimestamp().compareTo(eventSampleB.getTimestamp());
  }
  
}
