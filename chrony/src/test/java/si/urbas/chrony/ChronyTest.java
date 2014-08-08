package si.urbas.chrony;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChronyTest {

  private Chrony chrony;
  private EventRepository eventRepository;
  private ChronyReport chronyReport;

  @Before
  public void setUp() throws Exception {
    eventRepository = mock(EventRepository.class);
    chronyReport = mock(ChronyReport.class);
    ReportFactory reportFactory = mock(ReportFactory.class);
    when(reportFactory.createReport(eventRepository)).thenReturn(chronyReport);
    chrony = new Chrony(eventRepository, reportFactory);
  }

  @Test
  public void addEvent_MUST_add_the_event_to_the_event_repository() {
    Event event = new Event();
    chrony.addEvent(event);
    verify(eventRepository).addEvent(event);
  }

  @Test
  public void getReport_MUST_return_a_report_created_with_the_report_factory() {
    assertEquals(chronyReport, chrony.getReport());
  }

}
