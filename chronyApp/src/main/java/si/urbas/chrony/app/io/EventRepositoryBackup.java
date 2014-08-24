package si.urbas.chrony.app.io;

import android.os.Environment;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import si.urbas.chrony.Event;
import si.urbas.chrony.EventRepository;

import java.io.*;

public class EventRepositoryBackup {

  private static final String EVENT_JSON_FIELD_NAME = "name";
  private static final String EVENT_JSON_FIELD_TIMESTAMP = "timestamp";

  public static File storeToFile(String backupFileName, EventRepository eventRepository) {
    File backupFile = getBackupFilePath(backupFileName);
    backupFile.getParentFile().mkdirs();
    Writer fileWriter = null;
    try {
      fileWriter = new OutputStreamWriter(new FileOutputStream(backupFile));
      appendEventsJsonArrayToFile(fileWriter, eventRepository);
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
        appendEventsFromFileToEventRepository(fileReader, eventRepository);
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

  private static void appendEventsFromFileToEventRepository(InputStreamReader fileReader, EventRepository eventRepository) throws IOException {
    JsonReader jsonReader = new JsonReader(fileReader);
    jsonReader.beginArray();
    while (jsonReader.hasNext()) {
      jsonReader.beginObject();
      extractSingleEvent(eventRepository, jsonReader);
      jsonReader.endObject();
    }
    jsonReader.endArray();
  }

  private static void extractSingleEvent(EventRepository eventRepository, JsonReader jsonReader) throws IOException {
    String eventName = null;
    Long eventTimestamp = null;
    while (eventName == null || eventTimestamp == null) {
      if (!jsonReader.hasNext()) {
        throw new RuntimeException("The backup file is incorrectly formatted.");
      }
      String eventField = jsonReader.nextName();
      if (EVENT_JSON_FIELD_NAME.equals(eventField)) {
        eventName = jsonReader.nextString();
      } else if (EVENT_JSON_FIELD_TIMESTAMP.equals(eventField)) {
        eventTimestamp = jsonReader.nextLong();
      }
    }
    eventRepository.addEvent(new Event(eventName, eventTimestamp));
  }

  private static void appendEventsJsonArrayToFile(Writer outputWriter, EventRepository eventRepository) throws IOException {
    JsonWriter jsonWriter = new JsonWriter(outputWriter);
    jsonWriter.beginArray();
    for (String eventName : eventRepository.allEvents()) {
      for (Long eventTimestamp : eventRepository.timestampsOf(eventName)) {
        jsonWriter.beginObject()
                  .name(EVENT_JSON_FIELD_NAME).value(eventName)
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
