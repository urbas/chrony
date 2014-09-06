package si.urbas.chrony.app.io;

import android.util.JsonReader;
import si.urbas.chrony.Event;
import si.urbas.chrony.util.EventBuilder;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.EventSample;

import java.io.IOException;
import java.io.Reader;

import static si.urbas.chrony.Event.NO_DATA_TYPE;

public class EventsJsonReader {

  public static void loadJsonEventsToRepository(Reader sourceReader, EventRepository targetEventRepository) throws IOException {
    JsonReader jsonReader = new JsonReader(sourceReader);
    jsonReader.beginArray();
    EventBuilder eventBuilder = new EventBuilder();
    while (jsonReader.hasNext()) {
      loadJsonEventToRepository(targetEventRepository, jsonReader, eventBuilder);
    }
    jsonReader.endArray();
  }

  private static void loadJsonEventToRepository(EventRepository targetEventRepository, JsonReader jsonReader, EventBuilder eventBuilder) throws IOException {
    jsonReader.beginObject();
    eventBuilder.clear();
//    if (EventsJsonWriter.EVENT_JSON_FIELD_NAME.equals(eventNameField)) {
//      // OK
//      String eventName = jsonReader.nextString();
//    } else {
//      throw new IllegalArgumentException();
//    }
    loadJsonEventSamplesToRepository(jsonReader, targetEventRepository);
    jsonReader.endObject();
  }

  private static void loadJsonEventSamplesToRepository(JsonReader sourceJsonReader, EventRepository targetEventRepository) throws IOException {
    String eventName = null;
    Long eventTimestamp = null;
    while (eventName == null || eventTimestamp == null) {
      if (!sourceJsonReader.hasNext()) {
        throw new RuntimeException("The backup file is incorrectly formatted.");
      }
      String eventField = sourceJsonReader.nextName();
      if (EventsJsonWriter.EVENT_JSON_FIELD_NAME.equals(eventField)) {
        eventName = sourceJsonReader.nextString();
      } else if (EventsJsonWriter.EVENT_JSON_FIELD_TIMESTAMP.equals(eventField)) {
        eventTimestamp = sourceJsonReader.nextLong();
      }
    }
    targetEventRepository.addEvent(new Event(eventName, NO_DATA_TYPE));
    targetEventRepository.addEventSample(new EventSample(eventName, eventTimestamp, null));
  }

}
