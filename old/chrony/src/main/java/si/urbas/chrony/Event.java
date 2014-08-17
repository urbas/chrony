package si.urbas.chrony;

public class Event {

  public final String name;
  /**
   * The instant in which this event occurred.
   * The number of milliseconds since January 1, 1970, 00:00:00 GMT.
   */
  public final long timestamp;

  public Event(String name, long timestamp) {
    this.name = name;
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null || getClass() != o.getClass()) {
      return false;
    } else {
      Event that = (Event) o;
      return equals(that);
    }
  }

  public boolean equals(Event that) {
    return name.equals(that.name) &&
           timestamp == that.timestamp;
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
    return result;
  }
}
