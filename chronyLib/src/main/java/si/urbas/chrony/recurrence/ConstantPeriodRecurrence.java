package si.urbas.chrony.recurrence;

public class ConstantPeriodRecurrence implements Recurrence {
  private final int periodInDays;
  private final int hourOfDay;
  private final int minutesPastHour;

  public ConstantPeriodRecurrence(int periodInDays, int hourOfDay, int minutesPastHour) {
    this.periodInDays = periodInDays;
    this.hourOfDay = hourOfDay;
    this.minutesPastHour = minutesPastHour;
  }

  @Override
  public int getHourOfDay() {
    return hourOfDay;
  }

  @Override
  public int getMinutesPastHour() {
    return minutesPastHour;
  }

  @Override
  public int getPeriodInDays() {
    return periodInDays;
  }
}
