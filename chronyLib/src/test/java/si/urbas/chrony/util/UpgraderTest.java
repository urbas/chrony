package si.urbas.chrony.util;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class UpgraderTest {

  private static final int VERSION_0 = 0;
  private static final int VERSION_1 = 1;
  private static final int VERSION_2 = 2;
  private static final int VERSION_4 = 4;
  private static final String UPGRADE_DATA = "upgrade data";
  private Upgrader<String> upgrader;
  private UpgradeScript<String> version1;
  private UpgradeScript<String> version2;
  private UpgradeScript<String> version3;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() throws Exception {
    version1 = mock(UpgradeScript.class);
    version2 = mock(UpgradeScript.class);
    version3 = mock(UpgradeScript.class);
    upgrader = new Upgrader<String>()
      .add(version1)
      .add(version2)
      .add(version3);
  }

  @Test
  public void upgrade_MUST_call_the_upgrade_script_for_the_first_version() {
    upgrader.upgrade(UPGRADE_DATA, VERSION_0, VERSION_1);
    verify(version1).upgrade(UPGRADE_DATA);
  }

  @Test
  public void upgrade_MUST_call_the_upgrade_script_for_the_second_version() {
    upgrader.upgrade(UPGRADE_DATA, VERSION_1, VERSION_2);
    verify(version2).upgrade(UPGRADE_DATA);
  }

  @Test
  public void upgrade_MUST_call_only_the_second_upgrade_script_WHEN_upgrading_from_first_to_second_version() {
    upgrader.upgrade(UPGRADE_DATA, VERSION_1, VERSION_2);
    verifyZeroInteractions(version1);
    verifyZeroInteractions(version3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void upgrade_MUST_throw_an_exception_WHEN_an_upgrade_script_for_the_given_version_does_not_exist() {
    upgrader.upgrade(UPGRADE_DATA, VERSION_1, VERSION_4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void upgrade_MUST_throw_an_exception_WHEN_the_current_version_is_negative() {
    upgrader.upgrade(UPGRADE_DATA, -VERSION_1, VERSION_2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void upgrade_MUST_throw_an_exception_WHEN_the_current_version_is_larger_than_the_new_version() {
    upgrader.upgrade(UPGRADE_DATA, VERSION_2, VERSION_1);
  }

  @Test
  public void upgrade_MUST_not_invoke_any_scripts_WHEN_the_current_version_the_same_as_the_new_version() {
    upgrader.upgrade(UPGRADE_DATA, VERSION_2, VERSION_2);
    verifyZeroInteractions(version1);
    verifyZeroInteractions(version2);
    verifyZeroInteractions(version3);
  }

}