package si.urbas.chrony.util;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ChangeNotifierTest {

  private ChangeNotifier changeNotifier;
  private ChangeListener changeListener;

  @Before
  public void setUp() throws Exception {
    changeNotifier = new ChangeNotifier();
    changeListener = mock(ChangeListener.class);
  }

  @Test
  public void notifyChangeListeners_MUST_notify_the_registered_change_listeners() {
    changeNotifier.registerChangeListener(changeListener);
    changeNotifier.notifyChangeListeners();
    verify(changeListener).changed();
  }

  @Test
  public void notifyChangeListeners_MUST_not_notify_unregistered_change_listeners() {
    changeNotifier.registerChangeListener(changeListener);
    changeNotifier.unregisterChangeListener(changeListener);
    changeNotifier.notifyChangeListeners();
    verifyZeroInteractions(changeListener);
  }

}