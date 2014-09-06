package si.urbas.chrony.util;

import si.urbas.chrony.Event;

public class EventBuilder {
  private String eventName;
  private int eventDataType;
  private boolean isEventDataSpecified = false;

  public EventBuilder clear() {
    eventName = null;
    isEventDataSpecified = false;
    return this;
  }

  public Event create() {
    if (eventName == null || !isDataTypeSpecified()) {
      throw new UnsupportedOperationException();
    }
    return new Event(eventName, eventDataType);
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

  public boolean isDataTypeSpecified() {
    return isEventDataSpecified;
  }

  public boolean isNameSpecified() {
    return eventName != null;
  }
}
