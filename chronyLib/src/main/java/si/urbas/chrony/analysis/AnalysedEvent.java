package si.urbas.chrony.analysis;

import si.urbas.chrony.Event;

public interface AnalysedEvent {

  Event getUnderlyingEvent();

  int getCount();

  /**
   * @return the higher the number, the more relevant the event.
   */
  float getRelevance();
}
