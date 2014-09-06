package si.urbas.chrony.util;

import org.junit.Before;
import org.junit.Test;
import si.urbas.chrony.Event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
  public void creating_an_event_with_a_name_and_a_data_type_MUST_return_a_fully_specified_event() {
    Event event = eventBuilder.withName(EVENT_NAME).withDataType(Event.NUMBER_DATA_TYPE).create();
    assertEventEqualsTo(event, EVENT_NAME, Event.NUMBER_DATA_TYPE);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void create_MUST_throw_an_exception_AFTER_clearing_the_builder() {
    eventBuilder.withName(EVENT_NAME).withDataType(Event.NUMBER_DATA_TYPE).clear().create();
  }

  @Test
  public void isNameSpecified_MUST_return_true_AFTER_setting_the_name() {
    assertTrue(eventBuilder.withName(EVENT_NAME).isNameSpecified());
  }

  @Test
  public void isNameSpecified_MUST_return_false_AFTER_clearing_the_builder() {
    assertFalse(eventBuilder.withName(EVENT_NAME).clear().isNameSpecified());
  }

  @Test
  public void isDataTypeSpecified_MUST_return_true_AFTER_setting_the_name() {
    assertTrue(eventBuilder.withDataType(Event.NO_DATA_TYPE).isDataTypeSpecified());
  }

  @Test
  public void isDataTypeSpecified_MUST_return_false_AFTER_clearing_the_builder() {
    assertFalse(eventBuilder.withDataType(Event.NO_DATA_TYPE).clear().isNameSpecified());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void creating_an_event_with_only_a_name_and_no_data_type_MUST_throw_an_exception() {
    eventBuilder.withName(EVENT_NAME).create();
  }

  private static void assertEventEqualsTo(Event event, String expectedEventName, int expectedEventDataType) {
    assertEquals(expectedEventName, event.getEventName());
    assertEquals(expectedEventDataType, event.getDataType());
  }

}