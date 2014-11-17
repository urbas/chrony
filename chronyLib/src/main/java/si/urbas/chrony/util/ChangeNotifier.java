package si.urbas.chrony.util;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ChangeNotifier {

  private final ConcurrentLinkedQueue<ChangeListener> changeListeners = new ConcurrentLinkedQueue<ChangeListener>();

  public void registerChangeListener(ChangeListener changeListener) {
    changeListeners.add(changeListener);
  }

  public void unregisterChangeListener(ChangeListener changeListener) {
    changeListeners.remove(changeListener);
  }

  public void notifyChangeListeners() {
    for (ChangeListener changeListener : changeListeners) {
      changeListener.changed();
    }
  }
}