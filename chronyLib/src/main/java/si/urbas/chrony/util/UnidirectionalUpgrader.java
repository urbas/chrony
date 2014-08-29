package si.urbas.chrony.util;

import java.util.ArrayList;

public class UnidirectionalUpgrader<T> {

  private final ArrayList<UpgradeScript<T>> upgradeScripts = new ArrayList<UpgradeScript<T>>();

  public UnidirectionalUpgrader<T> add(UpgradeScript<T> upgradeScript) {
    upgradeScripts.add(upgradeScript);
    return this;
  }

  public void upgrade(T upgradeData, int currentVersion, int newVersion) {
    if (newVersion > upgradeScripts.size()) {
      throw new IllegalArgumentException("Could not upgrade to version " + newVersion + ". The version is too high.");
    }
    if (currentVersion < 0) {
      throw new IllegalArgumentException("Current version must be non-negative.");
    }
    if (currentVersion > newVersion) {
      throw new IllegalArgumentException("The new version is greater than the current version. Cannot downgrade.");
    }
    for (int i = currentVersion; i < newVersion; i++) {
      upgradeScripts.get(i).upgrade(upgradeData);
    }
  }
}