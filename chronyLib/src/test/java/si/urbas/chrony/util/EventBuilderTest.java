package si.urbas.chrony.util;

import org.junit.Before;
import org.junit.Test;
import si.urbas.chrony.Event;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class EventBuilderTest {

  private static final String EVENT_NAME = "event name";
  private EventBuilder eventBuilder;

  @Before
  public void setUp() throws Exception {
    eventBuilder = new EventBuilder();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void creating_an_event_without_any_data_MUST_throw_an_exception() {
    eventBuilder.create();
  }

  @Test
  public void create_MUST_return_a_fully_specified_event_WHEN_given_a_name_and_a_data_type() {
    assertEventHas(initialiseNumberEvent().create(), EVENT_NAME, Event.NUMBER_DATA_TYPE);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void create_MUST_throw_an_exception_AFTER_clearing_the_builder() {
    initialiseNumberEvent().clear().create();
  }

  @Test
  public void getEventSampleBuilder_MUST_return_a_builder() {
    assertThat(eventBuilder.getEventSampleBuilder(), is(instanceOf(EventBuilder.EventSampleBuilder.class)));
  }

  @Test
  public void getEventSampleBuilder_MUST_return_the_same_builder_WHEN_called_twice() {
    assertSame(eventBuilder.getEventSampleBuilder(), eventBuilder.getEventSampleBuilder());
  }

  private EventBuilder initialiseNumberEvent() {
    return eventBuilder.withName(EVENT_NAME).withDataType(Event.NUMBER_DATA_TYPE);
  }

  private static void assertEventHas(Event event, String expectedEventName, int expectedEventDataType) {
    assertEquals(expectedEventName, event.getEventName());
    assertEquals(expectedEventDataType, event.getDataType());
  }

}