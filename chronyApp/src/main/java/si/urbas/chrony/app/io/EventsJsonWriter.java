package si.urbas.chrony.app.io;

import android.util.JsonWriter;
import si.urbas.chrony.Event;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.EventSample;

import java.io.IOException;
import java.io.Writer;

public class EventsJsonWriter {

  public static void writeEvents(EventRepository sourceEventRepository, Writer targetWriter) throws IOException {
    JsonWriter jsonWriter = new JsonWriter(targetWriter);
    jsonWriter.beginArray();
    for (Event event : sourceEventRepository.allEvents()) {
      writeEvent(sourceEventRepository, jsonWriter, event);
    }
    jsonWriter.endArray();
  }

  private static void writeEvent(EventRepository sourceEventRepository, JsonWriter jsonWriter, Event event) throws IOException {
    jsonWriter.beginArray()
              .value(event.getEventName())
              .value(event.getDataType());
    writeEventSamples(sourceEventRepository, event, jsonWriter);
    jsonWriter.endArray();
  }

  private static void writeEventSamples(EventRepository sourceEventRepository, Event event, JsonWriter jsonObjectWriter) throws IOException {
    jsonObjectWriter.beginArray();
    for (EventSample eventTimestamp : sourceEventRepository.samplesOf(event.getEventName())) {
      jsonObjectWriter.beginArray()
                      .value(eventTimestamp.getTimestamp());
      writeEventSampleData(event, eventTimestamp, jsonObjectWriter);
      jsonObjectWriter.endArray();
    }
    jsonObjectWriter.endArray();
  }

  private static void writeEventSampleData(Event event, EventSample eventTimestamp, JsonWriter jsonObjectWriter) throws IOException {
    switch (event.getDataType()) {
      case Event.NO_DATA_TYPE:
        break;
      case Event.NUMBER_DATA_TYPE:
        jsonObjectWriter.value((Double) eventTimestamp.getData());
        break;
      default:
        throw new IllegalArgumentException("Cannot write event samples to file. Found an unknown data type.");
    }
  }
}
