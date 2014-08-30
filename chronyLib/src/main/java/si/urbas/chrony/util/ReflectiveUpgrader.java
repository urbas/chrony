package si.urbas.chrony.util;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ReflectiveUpgrader<T> {

  private final String upgradeMethodsNamePrefix;
  private final Object instanceWithUpgradeMethods;

  public ReflectiveUpgrader(String upgradeMethodsNamePrefix, Object instanceWithUpgradeMethods) {
    this.upgradeMethodsNamePrefix = upgradeMethodsNamePrefix;
    this.instanceWithUpgradeMethods = instanceWithUpgradeMethods;
  }

  public void upgrade(T upgradeData, int currentVersion, int newVersion) {
    assertVersionsAreSane(currentVersion, newVersion);
    invokeUpgradeMethods(upgradeData, findUpgradeMethods(currentVersion, newVersion, upgradeData));
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

  private ArrayList<Method> findUpgradeMethods(int currentVersion, int newVersion, T upgradeData) {
    ArrayList<Method> upgradeMethods = new ArrayList<Method>();
    for (int i = currentVersion; i < newVersion; i++) {
      try {
        upgradeMethods.add(findUpgradeMethod(i + 1, upgradeData));
      } catch (NoSuchMethodException e) {
        throw new IllegalArgumentException("Could not upgrade. The upgrade method for version " + i + " does not exist.");
      }
    }
    return upgradeMethods;
  }

  private Method findUpgradeMethod(int versionNumber, T upgradeData) throws NoSuchMethodException {
    return instanceWithUpgradeMethods.getClass().getMethod(upgradeMethodsNamePrefix + versionNumber, upgradeData.getClass());
  }

  private static void assertVersionsAreSane(int currentVersion, int newVersion) {
    if (currentVersion < 0) {
      throw new IllegalArgumentException("Current version must be non-negative.");
    }
    if (currentVersion > newVersion) {
      throw new IllegalArgumentException("The new version is greater than the current version. Cannot downgrade.");
    }
  }
}
