package si.urbas.chrony;

public class Event {

  public static final int NO_DATA_TYPE = 0;
  public static final int NUMBER_DATA_TYPE = 1;

  private final String eventName;
  private final int dataType;

  public Event(String eventName, int dataType) {
    assertEventNameValid(eventName);
    assertDataTypeIsKnown(dataType);
    this.eventName = eventName;
    this.dataType = dataType;
  }

  public String getEventName() {
    return eventName;
  }

  public int getDataType() {
    return dataType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (o == null || getClass() != o.getClass()) { return false; }

    Event event = (Event) o;

    return eventName.equals(event.eventName);

  }

  public static boolean isDataTypeKnown(int dataType) {
    return dataType == NO_DATA_TYPE || dataType == NUMBER_DATA_TYPE;
  }

  private static void assertEventNameValid(String eventName) {
    if (eventName == null) {
      throw new IllegalArgumentException("The event name must not be null.");
    }
  }

  private static void assertDataTypeIsKnown(int dataType) {
    if (!isDataTypeKnown(dataType)) {
      throw new IllegalArgumentException("Unknown event data type.");
    }
  }

  @Override
  public int hashCode() {
    return eventName.hashCode();
  }
}
