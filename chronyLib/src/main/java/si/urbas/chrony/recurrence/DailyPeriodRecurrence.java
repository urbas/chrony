package si.urbas.chrony.recurrence;

public class DailyPeriodRecurrence implements Recurrence {

  private final int periodInDays;
  private final int hourOfDay;
  private final int minutesPastHour;

  public DailyPeriodRecurrence(int periodInDays, int hourOfDay, int minutesPastHour) {
    this.periodInDays = periodInDays;
    this.hourOfDay = hourOfDay;
    this.minutesPastHour = minutesPastHour;
  }

  public int getHourOfDay() {
    return hourOfDay;
  }

  public int getMinutesPastHour() {
    return minutesPastHour;
  }

  public int getPeriodInDays() {
    return periodInDays;
  }
}
