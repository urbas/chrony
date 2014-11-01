package si.urbas.chrony.util;

import si.urbas.chrony.Event;
import si.urbas.chrony.EventSample;

/**
 * This class is not thread-safe.
 */
public class EventBuilder {

  private final EventSampleBuilder eventSampleBuilder = new EventSampleBuilder();
  private String eventName;
  private int eventDataType;
  private boolean isEventDataSpecified = false;

  public EventBuilder clear() {
    eventName = null;
    isEventDataSpecified = false;
    return this;
  }

  public Event create() {
    if (eventName != null && isEventDataSpecified) {
      return new Event(eventName, eventDataType);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  public EventBuilder withName(String eventName) {
    this.eventName = eventName;
    return this;
  }

  public EventBuilder withDataType(int dataType) {
    this.eventDataType = dataType;
    isEventDataSpecified = true;
    return this;
  }

  public EventSampleBuilder getEventSampleBuilder() {
    return eventSampleBuilder;
  }

  /**
   * This class is mutable and is not thread-safe.
   */
  public class EventSampleBuilder {

    private long timestamp;
    private Object data;
    private boolean isDataSpecified;
    private boolean isTimestampSpecified;

    public EventSampleBuilder clear() {
      isTimestampSpecified = false;
      isDataSpecified = false;
      return this;
    }

    public EventSampleBuilder withTimestamp(long timestamp) {
      this.timestamp = timestamp;
      isTimestampSpecified = true;
      return this;
    }

    public EventSampleBuilder withData(Object data) {
      this.data = data;
      isDataSpecified = true;
      return this;
    }

    public int getDataType() {
      return EventBuilder.this.eventDataType;
    }

    public EventSample create() {
      if (isTimestampSpecified && isDataSpecified && Event.isDataValid(eventDataType, data)) {
        return new EventSample(eventName, timestamp, data);
      }
      throw new UnsupportedOperationException("Cannot create the event sample. Some data is missing or invalid.");
    }
  }
}
