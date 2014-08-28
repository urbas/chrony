package si.urbas.chrony;

import java.util.Date;

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
  /**
   * Initialises this event with the given name and the current timestamp.
   */
  public Event(String name) {
    this(name, new Date().getTime());
  }
}
