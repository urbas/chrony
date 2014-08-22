package si.urbas.chrony.impl;

public class GlobalMetrics {

  public final long oldestTimestamp;
  public final long newestTimestamp;
  public final long oldestEventRefreshTimestamp;

  public GlobalMetrics(long oldestTimestamp, long newestTimestamp, long oldestEventRefreshTimestamp) {
    this.oldestTimestamp = oldestTimestamp;
    this.newestTimestamp = newestTimestamp;
    this.oldestEventRefreshTimestamp = oldestEventRefreshTimestamp;
  }
}
