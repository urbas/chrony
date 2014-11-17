package si.urbas.chrony.analysis;

import si.urbas.chrony.Event;

public class SimpleAnalysedEvent implements AnalysedEvent {

  private final int count;
  private final float relevance;
  private final Event event;

  public SimpleAnalysedEvent(Event event, int count, float relevance) {
    this.event = event;
    this.count = count;
    this.relevance = relevance;
  }

  @Override
  public int getCount() {
    return count;
  }

  @Override
  public float getRelevance() {
    return relevance;
  }

  @Override
  public Event getUnderlyingEvent() {
    return event;
  }
}
