package si.urbas.chrony;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;

public class ByNameEventGrouperTest {

  private static final String TEST_EVENT_NAME = "Test event name";
  private static final String TEST_EVENT_2_NAME = "test event 2 name";
  private final ByNameEventGrouper byNameEventGrouper = new ByNameEventGrouper();
  private MemoryEventRepository eventRepository;

  @Before
  public void setUp() throws Exception {
    eventRepository = new MemoryEventRepository();
  }

  @Test
  public void extractEventGroups_MUST_return_an_empty_list_WHEN_the_event_repository_is_empty() {
    Iterable<Event> events = byNameEventGrouper.extractEventGroups(eventRepository);
    assertThat(events, emptyIterable());
  }

  @Test
  public void extractEventGroups_MUST_return_a_signle_event_WHEN_the_event_repository_contains_a_single_event_sample() {
    eventRepository.addEvent(new EventSample(TEST_EVENT_NAME));
    Iterable<Event> events = byNameEventGrouper.extractEventGroups(eventRepository);
    assertThat(events, contains(new Event(TEST_EVENT_NAME)));
  }

  @Test
  public void extractEventGroups_MUST_return_a_signle_event_WHEN_the_event_repository_contains_two_event_samples_with_equal_names() {
    eventRepository.addEvent(new EventSample(TEST_EVENT_NAME));
    eventRepository.addEvent(new EventSample(TEST_EVENT_NAME));
    Iterable<Event> events = byNameEventGrouper.extractEventGroups(eventRepository);
    assertThat(events, contains(new Event(TEST_EVENT_NAME)));
  }

  @Test
  public void extractEventGroups_MUST_return_a_two_events_WHEN_the_event_repository_contains_two_event_samples_with_different_names() {
    eventRepository.addEvent(new EventSample(TEST_EVENT_NAME));
    eventRepository.addEvent(new EventSample(TEST_EVENT_NAME));
    eventRepository.addEvent(new EventSample(TEST_EVENT_2_NAME));
    Iterable<Event> events = byNameEventGrouper.extractEventGroups(eventRepository);
    assertThat(events, contains(new Event(TEST_EVENT_NAME), new Event(TEST_EVENT_2_NAME)));
  }
}