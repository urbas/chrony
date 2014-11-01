package si.urbas.chrony.util;

import si.urbas.chrony.EventSample;

import java.util.Comparator;

public class EventSampleTimestampComparator implements Comparator<EventSample> {

  public static final EventSampleTimestampComparator INSTANCE = new EventSampleTimestampComparator();

  @Override
  public int compare(EventSample eventSample, EventSample eventSample2) {
    return Long.compare(eventSample.getTimestampInMillis(), eventSample2.getTimestampInMillis());
  }
  
}
