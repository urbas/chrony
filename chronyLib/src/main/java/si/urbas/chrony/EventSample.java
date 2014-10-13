package si.urbas.chrony;

import si.urbas.chrony.util.TimeUtils;

import java.util.Calendar;
import java.util.Date;

public class EventSample {

  private final String eventName;
  private final long timestamp;
  private final Object data;
  private Calendar timestampAsCalendar;

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

  public Calendar getTimestampAsCalendar() {
    if (timestampAsCalendar == null) {
      timestampAsCalendar = TimeUtils.toUtcCalendar(timestamp);
    }
    return (Calendar) timestampAsCalendar.clone();
  }

  @Override
  public String toString() {
    return "EventSample{" +
           "eventName='" + eventName + '\'' +
           ", timestamp=" + TimeUtils.toSimpleString(getTimestampAsCalendar()) +
           ", data=" + data +
           '}';
  }
}
