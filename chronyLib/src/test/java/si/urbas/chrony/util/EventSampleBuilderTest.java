package si.urbas.chrony.util;

import org.junit.Before;
import org.junit.Test;
import si.urbas.chrony.Event;
import si.urbas.chrony.EventSample;

import static org.junit.Assert.assertEquals;

public class EventSampleBuilderTest {

  private static final String EVENT_NAME = "event name";
  private static final long TIMESTAMP_1 = 1L;
  private static final double SAMPLE_DATA_NUMBER_42_1 = 42.1;
  private EventBuilder eventBuilder;
  private EventBuilder.EventSampleBuilder eventSampleBuilder;

  @Before
  public void setUp() throws Exception {
    eventBuilder = new EventBuilder();
    eventSampleBuilder = eventBuilder.getEventSampleBuilder();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void create_MUST_throw_an_exception_WHEN_no_data_is_filled_in() {
    eventSampleBuilder.create();
  }

  @Test
  public void create_MUST_create_a_valid_event_sample_WHEN_all_data_is_specified() {
    initialiseNumberEvent();
    EventSample eventSample = initialiseNumberEventSample().create();
    assertEquals(EventSampleBuilderTest.EVENT_NAME, eventSample.getEventName());
    assertEquals(EventSampleBuilderTest.TIMESTAMP_1, eventSample.getTimestamp().getMillis());
    assertEquals(EventSampleBuilderTest.SAMPLE_DATA_NUMBER_42_1, eventSample.getData());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void create_MUST_throw_an_exception_WHEN_called_just_after_clearing() {
    initialiseNumberEvent();
    initialiseNumberEventSample().clear().create();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void create_MUST_throw_an_exception_WHEN_the_sample_data_does_not_correspond_to_the_event_data_type() {
    initialiseNumberEvent();
    eventSampleBuilder.withTimestamp(TIMESTAMP_1).withData(null).create();
  }

  @Test
  public void getEventDataType_MUST_return_the_data_type_specified_in_the_parent_event_builder() {
    initialiseNumberEvent();
    assertEquals(Event.NUMBER_DATA_TYPE, eventSampleBuilder.getDataType());
  }

  private void initialiseNumberEvent() {
    eventBuilder.withName(EVENT_NAME).withDataType(Event.NUMBER_DATA_TYPE);
  }

  private EventBuilder.EventSampleBuilder initialiseNumberEventSample() {
    return eventSampleBuilder.withTimestamp(TIMESTAMP_1).withData(SAMPLE_DATA_NUMBER_42_1);
  }

}