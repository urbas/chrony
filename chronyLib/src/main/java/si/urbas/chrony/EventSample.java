package si.urbas.chrony;

import java.util.Date;

public class EventSample {
  private final String eventName;
  private final long timestamp;
  private final Object data;

  public EventSample(String eventName, long timestamp, Object data) {
    this.eventName = eventName;
    this.timestamp = timestamp;
    this.data = data;
  }

  public EventSample(String eventName) {
    this(eventName, null);
  }

  public EventSample(String eventName, Object data) {
    this(eventName, new Date().getTime(), data);
  }

  public String getEventName() {
    return eventName;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public Object getData() {
    return data;
  }
}
