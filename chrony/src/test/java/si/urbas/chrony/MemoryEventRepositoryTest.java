package si.urbas.chrony;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class MemoryEventRepositoryTest {

  private static final String TEST_EVENT_NAME = "test event name";
  private static final EventSample EVENT_1_SAMPLE = new EventSample(TEST_EVENT_NAME, new Date());
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
      contains(TEST_EVENT_NAME)
    );
  }
}