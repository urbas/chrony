package si.urbas.chrony;

public interface AnalysedEvent {

  Event getUnderlyingEvent();

  int getCount();

  /**
   * @return the higher the number, the more relevant the event.
   */
  float getRelevance();
}
