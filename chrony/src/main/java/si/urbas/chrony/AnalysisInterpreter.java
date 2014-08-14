package si.urbas.chrony;

public interface AnalysisInterpreter {
  Iterable<EventDescription> findRelevantEvents(EventRepositoryAnalysis eventRepositoryAnalysis);
}
