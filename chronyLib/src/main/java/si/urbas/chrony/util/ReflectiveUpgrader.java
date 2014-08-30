package si.urbas.chrony.util;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ReflectiveUpgrader<T> {

  private final String upgradeMethodsNamePrefix;
  private final Object instanceWithUpgradeMethods;
  private final Class<T> upgradeMethodArgumentType;

  public ReflectiveUpgrader(String upgradeMethodsNamePrefix, Object instanceWithUpgradeMethods, Class<T> upgradeMethodArgumentType) {
    this.upgradeMethodsNamePrefix = upgradeMethodsNamePrefix;
    this.instanceWithUpgradeMethods = instanceWithUpgradeMethods;
    this.upgradeMethodArgumentType = upgradeMethodArgumentType;
  }

  public void upgrade(T upgradeData, int currentVersion, int newVersion) {
    assertVersionsSane(currentVersion, newVersion);
    invokeUpgradeMethods(upgradeData, getUpgradeMethods(currentVersion, newVersion));
  }

  private void invokeUpgradeMethods(T upgradeData, ArrayList<Method> upgradeMethods) {
    for (Method upgradeMethod : upgradeMethods) {
      invokeUpgradeMethod(upgradeData, upgradeMethod);
    }
  }

  private void invokeUpgradeMethod(T upgradeData, Method upgradeMethod) {
    try {
      upgradeMethod.invoke(instanceWithUpgradeMethods, upgradeData);
    } catch (Exception e) {
      throw new RuntimeException("An exception occurred when invoking the upgrade method '" + upgradeMethod.getName() + "'.", e);
    }
  }

  private ArrayList<Method> getUpgradeMethods(int currentVersion, int newVersion) {
    ArrayList<Method> upgradeMethods = new ArrayList<Method>();
    for (int i = currentVersion; i < newVersion; i++) {
      try {
        upgradeMethods.add(getUpgradeMethod(i + 1));
      } catch (NoSuchMethodException e) {
        throw new IllegalArgumentException("Could not upgrade. The upgrade method for version " + i + " does not exist.");
      }
    }
    return upgradeMethods;
  }

  private Method getUpgradeMethod(int versionNumber) throws NoSuchMethodException {
    return instanceWithUpgradeMethods.getClass().getMethod(upgradeMethodsNamePrefix + versionNumber, upgradeMethodArgumentType);
  }

  static void assertVersionsSane(int currentVersion, int newVersion) {
    if (currentVersion < 0) {
      throw new IllegalArgumentException("Current version must be non-negative.");
    }
    if (currentVersion > newVersion) {
      throw new IllegalArgumentException("The new version is greater than the current version. Cannot downgrade.");
    }
  }
}
