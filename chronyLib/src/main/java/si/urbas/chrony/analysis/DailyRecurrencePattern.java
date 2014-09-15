package si.urbas.chrony.analysis;

public class DailyRecurrencePattern {
  private final int periodInDays;

  public DailyRecurrencePattern(int periodInDays) {
    this.periodInDays = periodInDays;
  }

  public int getPeriod() {
    return periodInDays;
  }
}
