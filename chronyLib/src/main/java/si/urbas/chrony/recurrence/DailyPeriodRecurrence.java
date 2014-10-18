package si.urbas.chrony.recurrence;

import java.util.Calendar;

import static si.urbas.chrony.util.MathUtils.smallestByAbsoluteValue;
import static si.urbas.chrony.util.TimeUtils.*;

public class DailyPeriodRecurrence implements Recurrence {

  private final int periodInDays;
  private final Calendar firstOccurrence;

  public DailyPeriodRecurrence(int periodInDays, int year, int month, int dayOfMonth, int hourOfDay, int minutesPastHour) {
    this(periodInDays, toUtcCalendar(year, month, dayOfMonth, hourOfDay, minutesPastHour, 0));
  }

  public DailyPeriodRecurrence(int periodInDays, Calendar firstOccurrence) {
    this.periodInDays = periodInDays;
    this.firstOccurrence = firstOccurrence;
  }

  public int getPeriodInDays() {
    return periodInDays;
  }

  @Override
  public long distanceTo(long timeInMilliseconds) {
    long periodInMillis = DAY_IN_MILLIS * periodInDays;
    long firstRecurrenceTimeInMillis = firstOccurrence.getTimeInMillis();
    long distanceFromFirstOccurrence = timeInMilliseconds - firstRecurrenceTimeInMillis;
    long distanceToTimeInPeriods = distanceFromFirstOccurrence / periodInMillis;
    long distanceToUndershootingOccurrence = timeInMilliseconds - (firstRecurrenceTimeInMillis + distanceToTimeInPeriods * periodInMillis);
    boolean isTimeBeforeFirstOccurrence = distanceFromFirstOccurrence < 0;
    long distanceToOvershootingOccurrence = timeInMilliseconds - (firstRecurrenceTimeInMillis + (distanceToTimeInPeriods + (isTimeBeforeFirstOccurrence ? -1 : 1)) * periodInMillis);
    return smallestByAbsoluteValue(distanceToUndershootingOccurrence, distanceToOvershootingOccurrence);
  }

  @Override
  public OccurrenceList getOccurrencesBetween(final long fromTimeInMillis, final long toTimeInMillis) {
    return new RegularOccurrenceList(getFirstOccurrenceAfter(fromTimeInMillis), toTimeInMillis, periodInDays * DAY_IN_MILLIS);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (o == null || getClass() != o.getClass()) { return false; }

    DailyPeriodRecurrence that = (DailyPeriodRecurrence) o;

    return periodInDays == that.periodInDays && firstOccurrence.equals(that.firstOccurrence);
  }

  @Override
  public int hashCode() {
    int result = periodInDays;
    result = 31 * result + firstOccurrence.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "DailyPeriodRecurrence{" +
           "firstOccurrence=" + toSimpleString(firstOccurrence) +
           ", periodInDays=" + periodInDays +
           '}';
  }

  public Calendar getFirstOccurrence() {
    return firstOccurrence;
  }

  private long getFirstOccurrenceAfter(long timeInMillis) {
    long distanceToTime = distanceTo(timeInMillis);
    long nearestOccurrence = timeInMillis - distanceToTime;
    if (distanceToTime > 0) {
      return nearestOccurrence + periodInDays * DAY_IN_MILLIS;
    } else {
      return nearestOccurrence;
    }
  }

}
