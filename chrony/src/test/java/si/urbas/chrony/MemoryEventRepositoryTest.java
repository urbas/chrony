package si.urbas.chrony;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class MemoryEventRepositoryTest {

  private static final String TEST_EVENT_1_NAME = "test event 1 name";
  private static final String TEST_EVENT_2_NAME = "test event 2 name";
  private static final Event EVENT_1_SAMPLE = new Event(TEST_EVENT_1_NAME, new Date().getTime());
  private static final Event EVENT_2_SAMPLE = new Event(TEST_EVENT_2_NAME, new Date().getTime());
  private MemoryEventRepository memoryEventRepository;

  @Before
  public void setUp() throws Exception {
    memoryEventRepository = new MemoryEventRepository();
  }

  @Test
  public void allEvents_MUST_return_the_name_of_the_added_event() {
    memoryEventRepository.addEvent(EVENT_1_SAMPLE);
    assertThat(
      memoryEventRepository.allEvents(),
      contains(TEST_EVENT_1_NAME)
    );
  }

  @Test
  public void allEvents_MUST_return_two_names_of_two_added_events_with_different_names() {
    memoryEventRepository.addEvent(EVENT_1_SAMPLE);
    memoryEventRepository.addEvent(EVENT_1_SAMPLE);
    memoryEventRepository.addEvent(EVENT_2_SAMPLE);
    assertThat(
      memoryEventRepository.allEvents(),
      containsInAnyOrder(TEST_EVENT_1_NAME, TEST_EVENT_2_NAME)
    );
  }
}