package si.urbas.chrony;

import java.util.Date;

public class EventSample {

  public final String name;
  private final Date timestamp;

  public EventSample(String name, Date timestamp) {
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
      EventSample that = (EventSample) o;
      return equals(that);
    }
  }

  public boolean equals(EventSample that) {
    return name.equals(that.name) &&
           timestamp.equals(that.timestamp);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + timestamp.hashCode();
    return result;
  }
}
