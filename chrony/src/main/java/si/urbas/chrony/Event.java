package si.urbas.chrony;

public class Event {
  public final String name;

  public Event(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) { return true; }
    if (other == null || getClass() != other.getClass()) { return false; }

    Event otherEvent = (Event) other;

    return name.equals(otherEvent.name);

  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
