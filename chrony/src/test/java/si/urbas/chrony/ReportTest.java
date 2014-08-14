package si.urbas.chrony;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class ReportTest {

  private Report report;
  private Analyzer analyzer;
  private EventRepository eventRepository;
  private EventRepositoryAnalysis eventRepositoryAnalysis;
  private AnalysisInterpreter analysisInterpreter;
  private ArrayList<EventDescription> eventDescriptions;

  @Before
  public void setUp() throws Exception {
    eventRepository = mock(EventRepository.class);
    analyzer = mock(Analyzer.class);
    eventRepositoryAnalysis = mock(EventRepositoryAnalysis.class);
    analysisInterpreter = mock(AnalysisInterpreter.class);
    eventDescriptions = new ArrayList<>();

    doReturn(eventRepositoryAnalysis).when(analyzer).analyze(eventRepository);
    doReturn(eventDescriptions).when(analysisInterpreter).findRelevantEvents(eventRepositoryAnalysis);

    report = new Report(eventRepository, analyzer, analysisInterpreter);
  }

  @Test
  public void relevantEvents_MUST_pass_the_event_repository_to_the_event_analyzer() {
    report.relevantEvents();
    verify(analyzer).analyze(eventRepository);
  }

  @Test
  public void relevantEvents_MUST_pass_the_result_of_the_event_analysis_to_analysis_interpreter() {
    report.relevantEvents();
    verify(analysisInterpreter).findRelevantEvents(eventRepositoryAnalysis);
  }

  @Test
  public void relevantEvents_MUST_return_the_relevant_results_returned_by_the_analysis_interpreter() {
    assertSame(eventDescriptions, report.relevantEvents());
  }

}