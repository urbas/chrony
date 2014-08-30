package si.urbas.chrony.util;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ReflectiveUpgraderTest {

  private static final int VERSION_0 = 0;
  private static final int VERSION_1 = 1;
  private static final int VERSION_2 = 2;
  private static final int VERSION_4 = 4;
  private static final String UPGRADE_DATA = "upgrade data";
  private static final String UPGRADE_METHODS_NAME_PREFIX = "upgradeToVersion";
  private ReflectiveUpgrader<String> reflectiveUpgrader;
  private TestUpgradeScripts testUpgradeScripts;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() throws Exception {
    testUpgradeScripts = mock(TestUpgradeScripts.class);
    reflectiveUpgrader = new ReflectiveUpgrader<String>(UPGRADE_METHODS_NAME_PREFIX, testUpgradeScripts);
  }

  @Test
  public void upgrade_MUST_call_the_upgrade_script_for_the_first_version() {
    reflectiveUpgrader.upgrade(UPGRADE_DATA, VERSION_0, VERSION_1);
    verify(testUpgradeScripts).upgradeToVersion1(UPGRADE_DATA);
  }

  @Test
  public void upgrade_MUST_call_the_upgrade_script_for_the_second_version() {
    reflectiveUpgrader.upgrade(UPGRADE_DATA, VERSION_1, VERSION_2);
    verify(testUpgradeScripts).upgradeToVersion2(UPGRADE_DATA);
  }

  @Test
  public void upgrade_MUST_call_only_the_second_upgrade_script_WHEN_upgrading_from_first_to_second_version() {
    reflectiveUpgrader.upgrade(UPGRADE_DATA, VERSION_1, VERSION_2);
    verify(testUpgradeScripts, never()).upgradeToVersion1(any(String.class));
    verify(testUpgradeScripts, never()).upgradeToVersion3(any(String.class));
  }

  @Test(expected = IllegalArgumentException.class)
  public void upgrade_MUST_throw_an_exception_WHEN_an_upgrade_script_for_the_given_version_does_not_exist() {
    reflectiveUpgrader.upgrade(UPGRADE_DATA, VERSION_1, VERSION_4);
  }

  @Test
  public void upgrade_MUST_not_call_any_upgrade_scripts_WHEN_an_upgrade_script_for_a_new_version_does_not_exist() {
    try {
      reflectiveUpgrader.upgrade(UPGRADE_DATA, VERSION_1, VERSION_4);
    } catch (Exception ignored) {}
    verifyNoUpgradeMethodCalled();
  }

  @Test(expected = IllegalArgumentException.class)
  public void upgrade_MUST_throw_an_exception_WHEN_the_current_version_is_negative() {
    reflectiveUpgrader.upgrade(UPGRADE_DATA, -VERSION_1, VERSION_2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void upgrade_MUST_throw_an_exception_WHEN_the_current_version_is_larger_than_the_new_version() {
    reflectiveUpgrader.upgrade(UPGRADE_DATA, VERSION_2, VERSION_1);
  }

  @Test
  public void upgrade_MUST_not_invoke_any_scripts_WHEN_the_current_version_the_same_as_the_new_version() {
    reflectiveUpgrader.upgrade(UPGRADE_DATA, VERSION_2, VERSION_2);
    verifyNoUpgradeMethodCalled();
  }

  private void verifyNoUpgradeMethodCalled() {
    verify(testUpgradeScripts, never()).upgradeToVersion1(any(String.class));
    verify(testUpgradeScripts, never()).upgradeToVersion2(any(String.class));
    verify(testUpgradeScripts, never()).upgradeToVersion3(any(String.class));
  }

  private interface TestUpgradeScripts {
    void upgradeToVersion1(String upgradeData);

    void upgradeToVersion2(String upgradeData);

    void upgradeToVersion3(String upgradeData);
  }
}