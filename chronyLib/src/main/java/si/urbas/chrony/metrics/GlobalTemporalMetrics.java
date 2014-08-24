package si.urbas.chrony.metrics;

import java.util.List;

public class GlobalTemporalMetrics {

  public final long oldestTimestamp;
  public final long newestTimestamp;
  public final long oldestEventRefreshTimestamp;

  public GlobalTemporalMetrics(long oldestTimestamp, long newestTimestamp, long oldestEventRefreshTimestamp) {
    this.oldestTimestamp = oldestTimestamp;
    this.newestTimestamp = newestTimestamp;
    this.oldestEventRefreshTimestamp = oldestEventRefreshTimestamp;
  }

  public static GlobalTemporalMetrics calculate(List<EventTemporalMetrics> eventMetricsList) {
    if (eventMetricsList.size() == 0) {
      throw new IllegalArgumentException("Cannot calculate global statistics for an empty set of events.");
    }
    long oldestTimestamp = Long.MAX_VALUE;
    long newestTimestamp = Long.MIN_VALUE;
    long oldestEventRefreshTimestamp = Long.MAX_VALUE;
    for (EventTemporalMetrics perEventMetrics : eventMetricsList) {
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
    return new GlobalTemporalMetrics(oldestTimestamp, newestTimestamp, oldestEventRefreshTimestamp);
  }
}
