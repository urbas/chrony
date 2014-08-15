package si.urbas.chrony;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;

public class ReportIT {

  private static final String TEST_EVENT_NAME = "Test event";
  private Report report;
  private MemoryEventRepository eventRepository;

  @Before
  public void setUp() throws Exception {
    eventRepository = new MemoryEventRepository();
    FrequencyOnlyAnalyzer analyzer = new FrequencyOnlyAnalyzer(new ByNameEventGrouper());
    AnalysisInterpreter analysisInterpreter = new AnalysisInterpreter();
    report = new Report(eventRepository, analyzer, analysisInterpreter);
  }

  @Test
  public void relevantEvents_MUST_return_an_empty_list() {
    assertThat(report.relevantEvents(), emptyIterable());
  }

  @Test
  public void relevantEvents_MUST_return_the_event_descriptor_of_the_only_event_in_the_repository() {
    eventRepository.addEvent(new EventSample(TEST_EVENT_NAME));
    assertThat(report.relevantEvents(), contains(new Event(TEST_EVENT_NAME)));
  }

}
