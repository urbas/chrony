package si.urbas.chrony.util;

public class Upgrader<T> {

  private final UnidirectionalUpgrader<T> unidirectionalUpgrader = new UnidirectionalUpgrader<T>();

  public Upgrader<T> add(UpgradeScript<T> upgradeScript) {
    unidirectionalUpgrader.add(upgradeScript);
    return this;
  }

  public void upgrade(T upgradeData, int currentVersion, int newVersion) {
    unidirectionalUpgrader.upgrade(upgradeData, currentVersion, newVersion);
  }
}
