package si.urbas.chrony.util;

public interface UpgradeScript<T> {
  void upgrade(T upgradeData);
}
