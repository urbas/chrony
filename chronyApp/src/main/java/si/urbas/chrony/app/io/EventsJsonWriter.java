package si.urbas.chrony.app.io;

import android.util.JsonWriter;
import si.urbas.chrony.Event;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.EventSample;

import java.io.*;

public class EventsJsonWriter {

  public static final String EVENT_JSON_FIELD_NAME = "name";
  public static final String EVENT_JSON_FIELD_TIMESTAMP = "timestamp";
  public static final String EVENT_JSON_FIELD_DATA = "data";
  public static final String EVENT_JSON_FIELD_EVENT_SAMPLES = "samples";
  public static final String EVENT_JSON_FIELD_DATA_TYPE = "dataType";

  public static void writeEvents(EventRepository sourceEventRepository, Writer targetWriter) throws IOException {
    JsonWriter jsonWriter = new JsonWriter(targetWriter);
    jsonWriter.beginArray();
    for (Event event : sourceEventRepository.allEvents()) {
      writeEvent(sourceEventRepository, jsonWriter, event);
    }
    jsonWriter.endArray();
  }

  private static void writeEvent(EventRepository sourceEventRepository, JsonWriter jsonWriter, Event event) throws IOException {
    JsonWriter jsonObjectWriter = jsonWriter.beginObject();
    writeEventName(event, jsonObjectWriter);
    writeEventDataType(event, jsonObjectWriter);
    writeEventSamples(sourceEventRepository, event, jsonObjectWriter);
    jsonObjectWriter.endObject();
  }

  private static void writeEventDataType(Event event, JsonWriter jsonObjectWriter) throws IOException {
    jsonObjectWriter.name(EVENT_JSON_FIELD_DATA_TYPE).value(event.getDataType());
  }

  private static void writeEventSamples(EventRepository sourceEventRepository, Event event, JsonWriter jsonObjectWriter) throws IOException {
    jsonObjectWriter.name(EVENT_JSON_FIELD_EVENT_SAMPLES).beginArray();
    for (EventSample eventTimestamp : sourceEventRepository.samplesOf(event.getEventName())) {
      jsonObjectWriter.beginObject();
      writeEventSampleTimestamp(eventTimestamp, jsonObjectWriter);
      writeEventSampleData(eventTimestamp, jsonObjectWriter);
      jsonObjectWriter.endObject();
    }
    jsonObjectWriter.endArray();
  }

  private static JsonWriter writeEventSampleTimestamp(EventSample eventTimestamp, JsonWriter jsonObjectWriter) throws IOException {
    return jsonObjectWriter.name(EVENT_JSON_FIELD_TIMESTAMP).value(eventTimestamp.getTimestamp());
  }

  private static void writeEventName(Event event, JsonWriter jsonObjectWriter) throws IOException {
    jsonObjectWriter.name(EVENT_JSON_FIELD_NAME).value(event.getEventName());
  }

  private static void writeEventSampleData(EventSample eventTimestamp, JsonWriter jsonObjectWriter) throws IOException {
    Object eventSampleData = eventTimestamp.getData();
    if (eventSampleData == null) {
      jsonObjectWriter.name(EVENT_JSON_FIELD_DATA).nullValue();
    } else {
      jsonObjectWriter.name(EVENT_JSON_FIELD_DATA).value((Double) eventSampleData);
    }
  }
}
