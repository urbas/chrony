package si.urbas.chrony.analysis;

import java.util.List;

public class GlobalTimeMetrics {

  public final long oldestTimestamp;
  public final long newestTimestamp;
  public final long oldestEventRefreshTimestamp;

  public GlobalTimeMetrics(long oldestTimestamp, long newestTimestamp, long oldestEventRefreshTimestamp) {
    this.oldestTimestamp = oldestTimestamp;
    this.newestTimestamp = newestTimestamp;
    this.oldestEventRefreshTimestamp = oldestEventRefreshTimestamp;
  }

  public static GlobalTimeMetrics calculateGlobalMetrics(List<EventTimeMetrics> eventMetricsList) {
    if (eventMetricsList.size() == 0) {
      throw new IllegalArgumentException("Cannot calculate global statistics for an empty set of events.");
    }
    long oldestTimestamp = Long.MAX_VALUE;
    long newestTimestamp = Long.MIN_VALUE;
    long oldestEventRefreshTimestamp = Long.MAX_VALUE;
    for (EventTimeMetrics perEventMetrics : eventMetricsList) {
      if (perEventMetrics.newestTimestamp > newestTimestamp) {
        newestTimestamp = perEventMetrics.newestTimestamp;
      }
      if (perEventMetrics.oldestTimestamp < oldestTimestamp) {
        oldestTimestamp = perEventMetrics.oldestTimestamp;
      }
      if (perEventMetrics.newestTimestamp < oldestEventRefreshTimestamp) {
        oldestEventRefreshTimestamp = perEventMetrics.newestTimestamp;
      }
    }
    return new GlobalTimeMetrics(oldestTimestamp, newestTimestamp, oldestEventRefreshTimestamp);
  }
}
