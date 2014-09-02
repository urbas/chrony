package si.urbas.chrony;

public class Event {

  public static final int NO_DATA_TYPE = 0;
  public static final int NUMBER_DATA_TYPE = 1;

  private final String eventName;
  private final int dataType;

  public Event(String eventName, int dataType) {
    if (dataType < NO_DATA_TYPE || dataType > NUMBER_DATA_TYPE) {
      throw new IllegalArgumentException("Unknown event data type.");
    }
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
