package si.urbas.chrony;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FrequencyOnlyAnalyzerTest {

  private FrequencyOnlyAnalyzer frequencyOnlyAnalyzer;
  private EventRepository eventRepository;
  private EventGrouper eventGrouper;

  @Before
  public void setUp() throws Exception {
    eventRepository = mock(EventRepository.class);
    eventGrouper = mock(EventGrouper.class);
    frequencyOnlyAnalyzer = new FrequencyOnlyAnalyzer(eventGrouper);
  }

  @Test
  public void analyze_MUST_use_the_event_grouper() {
    frequencyOnlyAnalyzer.analyze(eventRepository);
    verify(eventGrouper).extractEventGroups(eventRepository);
  }

}