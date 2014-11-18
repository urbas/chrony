package si.urbas.chrony;

import org.joda.time.DateTime;

import static si.urbas.chrony.util.TimeUtils.*;

public class EventSample {

  private final String eventName;
  private final Object data;
  private final DateTime timestampDateTime;

  public EventSample(String eventName, long timestamp, Object data) {
    this(eventName, toUtcDate(timestamp), data);
  }

  public EventSample(String eventName, DateTime timestamp, Object data) {
    this.eventName = eventName;
    this.data = data;
    this.timestampDateTime = timestamp;
  }

  public EventSample(String eventName) {
    this(eventName, null);
  }

  public EventSample(String eventName, Object data) {
    this(eventName, createUtcDate(), data);
  }

  public String getEventName() {
    return eventName;
  }

  public Object getData() {
    return data;
  }

  public DateTime getTimestamp() {
    return timestampDateTime;
  }

  @Override
  public String toString() {
    return "EventSample{" +
           "eventName='" + eventName + '\'' +
           ", timestamp=" + formatDate(getTimestamp()) +
           ", data=" + data +
           '}';
  }
}
