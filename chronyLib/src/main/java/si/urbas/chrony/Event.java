package si.urbas.chrony;

public class Event {
  public final String name;
  public final long timestamp;

  /**
   * @param name uniquely identifies the event (used for grouping).
   * @param timestamp the number of milliseconds since epoch UTC (same as {@link java.util.Date#getTime()}).
   */
  public Event(String name, long timestamp) {
    this.name = name;
    this.timestamp = timestamp;
  }
}
