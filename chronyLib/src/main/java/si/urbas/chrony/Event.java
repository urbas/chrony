package si.urbas.chrony;

public class Event {

  public static final int NO_DATA_TYPE = 0;

  private final String eventName;
  private final int dataType;

  public Event(String eventName, int dataType) {
    this.eventName = eventName;
    this.dataType = dataType;
  }

  public String getEventName() {
    return eventName;
  }

  public int getDataType() {
    return dataType;
  }
}
