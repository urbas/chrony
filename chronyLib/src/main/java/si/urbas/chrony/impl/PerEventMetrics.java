package si.urbas.chrony.impl;

public class PerEventMetrics {

  public final String name;
  public final int count;
  public final long newestTimestamp;
  public final long oldestTimestamp;

  protected PerEventMetrics(String name, int count, long newestTimestamp, long oldestTimestamp) {
    this.name = name;
    this.count = count;
    this.newestTimestamp = newestTimestamp;
    this.oldestTimestamp = oldestTimestamp;
  }

}
