package si.urbas.chrony.util;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ChangeNotifier {

  private final ConcurrentLinkedQueue<ChangeListener> dataSetObservable = new ConcurrentLinkedQueue<ChangeListener>();

  public void registerChangeListener(ChangeListener changeListener) {
    dataSetObservable.add(changeListener);
  }

  public void unregisterChangeListener(ChangeListener changeListener) {
    dataSetObservable.remove(changeListener);
  }

  public void notifyChangeListeners() {
    for (ChangeListener changeListener : dataSetObservable) {
      changeListener.changed();
    }
  }
}