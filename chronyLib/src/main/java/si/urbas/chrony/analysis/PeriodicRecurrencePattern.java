package si.urbas.chrony.analysis;

import si.urbas.chrony.recurrence.Recurrence;

public class PeriodicRecurrencePattern implements Recurrence {

  private final int periodInDays;

  public PeriodicRecurrencePattern(int periodInDays) {
    this.periodInDays = periodInDays;
  }

  public int getPeriod() {
    return periodInDays;
  }
}
