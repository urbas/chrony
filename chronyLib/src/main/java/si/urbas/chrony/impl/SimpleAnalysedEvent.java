package si.urbas.chrony.impl;

import si.urbas.chrony.AnalysedEvent;

public class SimpleAnalysedEvent implements AnalysedEvent {
  private final String eventName;
  private final int count;
  private final float relevance;

  public SimpleAnalysedEvent(String eventName, int count, float relevance) {
    this.eventName = eventName;
    this.count = count;
    this.relevance = relevance;
  }

  @Override
  public String getEventName() {
    return eventName;
  }

  @Override
  public int getCount() {
    return count;
  }

  @Override
  public float getRelevance() {
    return relevance;
  }
}
