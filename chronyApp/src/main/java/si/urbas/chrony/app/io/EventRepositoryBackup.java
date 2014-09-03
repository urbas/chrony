package si.urbas.chrony.app.io;

import android.os.Environment;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import si.urbas.chrony.Event;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.EventSample;

import java.io.*;

import static si.urbas.chrony.Event.NO_DATA_TYPE;

public class EventRepositoryBackup {

  private static final String EVENT_JSON_FIELD_NAME = "name";
  private static final String EVENT_JSON_FIELD_TIMESTAMP = "timestamp";

  public static File storeToFile(String backupFileName, EventRepository eventRepository) {
    File backupFile = getBackupFilePath(backupFileName);
    backupFile.getParentFile().mkdirs();
    Writer fileWriter = null;
    try {
      fileWriter = new OutputStreamWriter(new FileOutputStream(backupFile));
      appendEventsToWriter(eventRepository, fileWriter);
    } catch (Throwable e) {
      throw new RuntimeException("Could not write the event repository to file.", e);
    } finally {
      if (fileWriter != null) {
        try {
          fileWriter.close();
        } catch (IOException e) {
          Log.v("closing failed", "Could not close the file after writing.", e);
        }
      }
    }
    return backupFile;
  }

  public static void restoreFromFile(String backupFileName, EventRepository eventRepository) {
    File backupFile = getBackupFilePath(backupFileName);
    if (backupFile.exists()) {
      InputStreamReader fileReader = null;
      try {
        fileReader = new InputStreamReader(new FileInputStream(backupFile));
        eventRepository.clear();
        appendEventsToRepository(fileReader, eventRepository);
      } catch (Throwable e) {
        throw new RuntimeException("Could not read the database from the backup file.", e);
      } finally {
        if (fileReader != null) {
          try {
            fileReader.close();
          } catch (IOException e) {
            Log.v("closing failed", "Could not close the file after reading.", e);
          }
        }
      }
    }
  }

  private static void appendEventsToRepository(Reader sourceReader, EventRepository targetEventRepository) throws IOException {
    JsonReader jsonReader = new JsonReader(sourceReader);
    jsonReader.beginArray();
    while (jsonReader.hasNext()) {
      jsonReader.beginObject();
      appendEventTimestampToRepository(jsonReader, targetEventRepository);
      jsonReader.endObject();
    }
    jsonReader.endArray();
  }

  private static void appendEventTimestampToRepository(JsonReader sourceJsonReader, EventRepository targetEventRepository) throws IOException {
    String eventName = null;
    Long eventTimestamp = null;
    while (eventName == null || eventTimestamp == null) {
      if (!sourceJsonReader.hasNext()) {
        throw new RuntimeException("The backup file is incorrectly formatted.");
      }
      String eventField = sourceJsonReader.nextName();
      if (EVENT_JSON_FIELD_NAME.equals(eventField)) {
        eventName = sourceJsonReader.nextString();
      } else if (EVENT_JSON_FIELD_TIMESTAMP.equals(eventField)) {
        eventTimestamp = sourceJsonReader.nextLong();
      }
    }
    targetEventRepository.addEvent(new Event(eventName, NO_DATA_TYPE));
    targetEventRepository.addEventSample(new EventSample(eventName, eventTimestamp, null));
  }

  private static void appendEventsToWriter(EventRepository sourceEventRepository, Writer targetWriter) throws IOException {
    JsonWriter jsonWriter = new JsonWriter(targetWriter);
    jsonWriter.beginArray();
    for (Event event : sourceEventRepository.allEvents()) {
      for (Long eventTimestamp : sourceEventRepository.timestampsOf(event.getEventName())) {
        jsonWriter.beginObject()
                  .name(EVENT_JSON_FIELD_NAME).value(event.getEventName())
                  .name(EVENT_JSON_FIELD_TIMESTAMP).value(eventTimestamp)
                  .endObject();
      }
    }
    jsonWriter.endArray();
  }

  private static File getBackupFilePath(String backupFileName) {
    File backupDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    return new File(backupDirectory, backupFileName);
  }
}
