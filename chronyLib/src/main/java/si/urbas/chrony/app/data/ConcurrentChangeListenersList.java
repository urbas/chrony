package si.urbas.chrony.app.data;

import si.urbas.chrony.ChangeListener;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentChangeListenersList {

  final ConcurrentLinkedQueue<ChangeListener> dataSetObservable = new ConcurrentLinkedQueue<ChangeListener>();

  public void registerChangeListener(final ChangeListener changeListener) {
    dataSetObservable.add(changeListener);
  }

  void notifyChangeListeners() {
    for (ChangeListener changeListener : dataSetObservable) {
      changeListener.changed();
    }
  }
}