package si.urbas.chrony.analysis;

public class PeriodicRecurrencePattern {
  private final int periodInDays;

  public PeriodicRecurrencePattern(int periodInDays) {
    this.periodInDays = periodInDays;
  }

  public int getPeriod() {
    return periodInDays;
  }
}
