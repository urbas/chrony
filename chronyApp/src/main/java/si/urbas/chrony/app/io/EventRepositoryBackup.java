package si.urbas.chrony.app.io;

import android.os.Environment;
import si.urbas.chrony.EventRepository;

import java.io.*;

import static si.urbas.chrony.app.io.CloseableUtils.tryClose;

public class EventRepositoryBackup {

  public static void restoreFromFile(String backupFileName, EventRepository eventRepository) {
    File backupFile = getBackupFilePath(backupFileName);
    if (backupFile.exists()) {
      BufferedReader fileReader = null;
      try {
        fileReader = new BufferedReader(new FileReader(backupFile));
        eventRepository.clear();
        EventsJsonReader.loadEvents(fileReader, eventRepository);
      } catch (Throwable e) {
        throw new RuntimeException("Could not read the database from the backup file.", e);
      } finally {
        tryClose(fileReader);
      }
    }
  }

  public static File storeToFile(String backupFileName, EventRepository eventRepository) {
    File backupFile = getBackupFilePath(backupFileName);
    backupFile.getParentFile().mkdirs();
    Writer fileWriter = null;
    try {
      fileWriter = new OutputStreamWriter(new FileOutputStream(backupFile));
      EventsJsonWriter.writeEvents(eventRepository, fileWriter);
    } catch (Throwable e) {
      throw new RuntimeException("Could not write the event repository to file.", e);
    } finally {
      tryClose(fileWriter);
    }
    return backupFile;
  }

  private static File getBackupFilePath(String backupFileName) {
    File backupDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    return new File(backupDirectory, backupFileName);
  }

}
