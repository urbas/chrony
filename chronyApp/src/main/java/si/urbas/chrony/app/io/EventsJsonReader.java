package si.urbas.chrony.app.io;

import android.util.JsonReader;
import si.urbas.chrony.Event;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.util.EventBuilder;

import java.io.IOException;
import java.io.Reader;

public class EventsJsonReader {

  public static void loadEvents(Reader sourceReader, EventRepository targetEventRepository) throws IOException {
    JsonReader jsonReader = new JsonReader(sourceReader);
    EventBuilder eventBuilder = new EventBuilder();
    jsonReader.beginArray();
    while (jsonReader.hasNext()) {
      readEvent(jsonReader, eventBuilder, targetEventRepository);
    }
    jsonReader.endArray();
  }

  private static void readEvent(JsonReader jsonReader, EventBuilder eventBuilder, EventRepository targetEventRepository) throws IOException {
    eventBuilder.clear();
    jsonReader.beginArray();
    eventBuilder.withName(jsonReader.nextString())
                .withDataType(jsonReader.nextInt());
    targetEventRepository.addEvent(eventBuilder.create());
    readEventSamples(jsonReader, eventBuilder, targetEventRepository);
    jsonReader.endArray();
  }

  private static void readEventSamples(JsonReader jsonReader, EventBuilder eventBuilder, EventRepository targetEventRepository) throws IOException {
    jsonReader.beginArray();
    while (jsonReader.hasNext()) {
      readEventSample(jsonReader, eventBuilder.getEventSampleBuilder(), targetEventRepository);
    }
    jsonReader.endArray();
  }

  private static void readEventSample(JsonReader jsonReader, EventBuilder.EventSampleBuilder eventSampleBuilder, EventRepository targetEventRepository) throws IOException {
    eventSampleBuilder.clear();
    jsonReader.beginArray();
    eventSampleBuilder.withTimestamp(jsonReader.nextLong());
    readEventSampleData(jsonReader, eventSampleBuilder);
    targetEventRepository.addEventSample(eventSampleBuilder.create());
    jsonReader.endArray();
  }

  private static void readEventSampleData(JsonReader jsonReader, EventBuilder.EventSampleBuilder eventSampleBuilder) throws IOException {
    switch (eventSampleBuilder.getDataType()) {
      case Event.NO_DATA_TYPE:
        eventSampleBuilder.withData(null);
        break;
      case Event.NUMBER_DATA_TYPE:
        eventSampleBuilder.withData(jsonReader.nextDouble());
        break;
      default:
        throw new IllegalArgumentException("Found an unknown event data type.");
    }
  }


}
