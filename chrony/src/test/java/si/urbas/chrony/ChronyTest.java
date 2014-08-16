package si.urbas.chrony;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class ChronyTest {

  private static final String TEST_EVENT_NAME = "Test event name";
  private Chrony chrony;
  private EventRepository eventRepository;
  private final HashSet<String> events = new HashSet<>();

  @Before
  public void setUp() throws Exception {
    eventRepository = mock(EventRepository.class);
    when(eventRepository.allEvents()).thenReturn(events);
    chrony = new Chrony(eventRepository);
  }

  @Test
  public void addEvent_MUST_add_the_event_to_the_event_repository() {
    EventSample eventSample = new EventSample(TEST_EVENT_NAME, new Date());
    chrony.addEvent(eventSample);
    verify(eventRepository).addEvent(eventSample);
  }

  @Test
  public void allEvents_MUST_return_the_names_of_added_events_as_returned_by_the_event_repository() {
    assertSame(chrony.allEvents(), events);
  }

}
