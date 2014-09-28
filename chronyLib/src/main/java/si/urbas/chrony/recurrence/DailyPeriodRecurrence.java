package si.urbas.chrony.recurrence;

import si.urbas.chrony.util.TimeUtils;

import java.util.Calendar;

import static si.urbas.chrony.util.MathUtils.smallestByAbsoluteValue;
import static si.urbas.chrony.util.TimeUtils.toUtcCalendar;

public class DailyPeriodRecurrence implements Recurrence {

  private final int periodInDays;
  private final Calendar firstRecurrenceTime;
  private final int hourOfDay;
  private final int minutePastHour;

  public DailyPeriodRecurrence(int periodInDays, int year, int month, int dayOfMonth, int hourOfDay, int minutesPastHour) {
    this(periodInDays, toUtcCalendar(year, month, dayOfMonth, hourOfDay, minutesPastHour, 0));
  }

  public DailyPeriodRecurrence(int periodInDays, Calendar firstRecurrenceTime) {
    this.periodInDays = periodInDays;
    this.firstRecurrenceTime = firstRecurrenceTime;
    hourOfDay = firstRecurrenceTime.get(Calendar.HOUR_OF_DAY);
    minutePastHour = firstRecurrenceTime.get(Calendar.MINUTE);
  }

  public int getHourOfDay() {
    return hourOfDay;
  }

  public int getMinutesPastHour() {
    return minutePastHour;
  }

  public int getPeriodInDays() {
    return periodInDays;
  }

  @Override
  public long distanceTo(long timeInMilliseconds) {
    long periodInMillis = TimeUtils.DAY_IN_MILLIS * periodInDays;
    long firstRecurrenceTimeInMillis = firstRecurrenceTime.getTimeInMillis();
    long distanceFromFirstOccurrence = timeInMilliseconds - firstRecurrenceTimeInMillis;
    long distanceToTimeInPeriods = distanceFromFirstOccurrence / periodInMillis;
    long distanceToUndershootingOccurrence = timeInMilliseconds - (firstRecurrenceTimeInMillis + distanceToTimeInPeriods * periodInMillis);
    boolean isTimeBeforeFirstOccurrence = distanceFromFirstOccurrence < 0;
    long distanceToOvershootingOccurrence = timeInMilliseconds - (firstRecurrenceTimeInMillis + (distanceToTimeInPeriods + (isTimeBeforeFirstOccurrence ? -1 : 1)) * periodInMillis);
    return smallestByAbsoluteValue(distanceToUndershootingOccurrence, distanceToOvershootingOccurrence);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (o == null || getClass() != o.getClass()) { return false; }

    DailyPeriodRecurrence that = (DailyPeriodRecurrence) o;

    return periodInDays == that.periodInDays && firstRecurrenceTime.equals(that.firstRecurrenceTime);
  }

  @Override
  public int hashCode() {
    int result = periodInDays;
    result = 31 * result + firstRecurrenceTime.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "DailyPeriodRecurrence{" +
           "firstRecurrenceTime=" + TimeUtils.toSimpleString(firstRecurrenceTime) +
           ", periodInDays=" + periodInDays +
           '}';
  }

}