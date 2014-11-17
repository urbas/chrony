package si.urbas.chrony.analysis;

import java.util.List;

public interface Analysis {
  /**
   * @return analysed events ordered by relevance (most relevant to least relevant).
   */
  List<AnalysedEvent> getAnalysedEvents();
}
