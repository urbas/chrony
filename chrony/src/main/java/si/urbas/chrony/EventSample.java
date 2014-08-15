package si.urbas.chrony;

public class EventSample {

  public final String name;

  public EventSample(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (o == null || getClass() != o.getClass()) { return false; }

    EventSample that = (EventSample) o;

    return name.equals(that.name);

  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
