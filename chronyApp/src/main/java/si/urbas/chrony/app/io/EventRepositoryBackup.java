package si.urbas.chrony.app.io;

import android.os.Environment;
import android.util.JsonWriter;
import android.util.Log;
import si.urbas.chrony.EventRepository;

import java.io.*;

public class EventRepositoryBackup {

  public static File storeToFile(String backupFileName, EventRepository eventRepository) {
    File backupDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    backupDirectory.mkdirs();
    File backupFile = new File(backupDirectory, backupFileName);
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

  private static void appendEventsJsonArrayToFile(Writer outputWriter, EventRepository eventRepository) throws IOException {
    JsonWriter jsonWriter = new JsonWriter(outputWriter);
    jsonWriter.beginArray();
    for (String eventName : eventRepository.allEvents()) {
      for (Long eventTimestamp : eventRepository.timestampsOf(eventName)) {
        jsonWriter.beginObject()
                  .name("eventName").value(eventName)
                  .name("timestamp").value(eventTimestamp)
                  .endObject();
      }
    }
    jsonWriter.endArray();
  }
}
