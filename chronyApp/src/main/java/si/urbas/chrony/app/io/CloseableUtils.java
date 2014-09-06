package si.urbas.chrony.app.io;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

public class CloseableUtils {
  public static void tryClose(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException e) {
        Log.v("closing failed", "Could not close the file.", e);
      }
    }
  }
}
