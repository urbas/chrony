package si.urbas.chrony.impl;

import java.util.ArrayList;

public class EventTimestamps {
  private final ArrayList<Long> timestamps = new ArrayList<Long>();

  public void addTimestamp(long timestamp) {
    timestamps.add(timestamp);
  }

  public int size() {
    return timestamps.size();
  }
}
