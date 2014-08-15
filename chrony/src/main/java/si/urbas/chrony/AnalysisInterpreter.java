package si.urbas.chrony;

public class AnalysisInterpreter {
  public Iterable<Event> mostRelevantEvents(Analysis analysis) {
    return analysis.allEvents();
  }
}
