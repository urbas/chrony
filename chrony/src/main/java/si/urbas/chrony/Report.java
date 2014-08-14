package si.urbas.chrony;

public class Report {
  private final EventRepository eventRepository;
  private final Analyzer analyzer;
  private final AnalysisInterpreter analysisInterpreter;

  public Report(EventRepository eventRepository, Analyzer analyzer, AnalysisInterpreter analysisInterpreter) {
    this.eventRepository = eventRepository;
    this.analyzer = analyzer;
    this.analysisInterpreter = analysisInterpreter;
  }

  public Iterable<EventDescription> relevantEvents() {
    EventRepositoryAnalysis eventRepositoryAnalysis = analyzer.analyze(eventRepository);
    return analysisInterpreter.findRelevantEvents(eventRepositoryAnalysis);
  }
}