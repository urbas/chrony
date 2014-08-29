package si.urbas.chrony.util;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentChangeListenersList {

  private final ConcurrentLinkedQueue<ChangeListener> dataSetObservable = new ConcurrentLinkedQueue<ChangeListener>();

  public void registerChangeListener(final ChangeListener changeListener) {
    dataSetObservable.add(changeListener);
  }

  public void notifyChangeListeners() {
    for (ChangeListener changeListener : dataSetObservable) {
      changeListener.changed();
    }
  }
}