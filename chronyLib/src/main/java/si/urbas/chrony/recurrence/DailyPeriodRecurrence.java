package si.urbas.chrony.recurrence;

import org.joda.time.DateTime;

import static si.urbas.chrony.util.MathUtils.smallestByAbsoluteValue;
import static si.urbas.chrony.util.TimeUtils.*;

public class DailyPeriodRecurrence implements Recurrence {

  private final int periodInDays;
  private final DateTime firstOccurrence;

  public DailyPeriodRecurrence(int periodInDays, int year, int month, int dayOfMonth, int hourOfDay, int minutesPastHour) {
    this(periodInDays, toUtcDate(year, month, dayOfMonth, hourOfDay, minutesPastHour, 0));
  }

  public DailyPeriodRecurrence(int periodInDays, DateTime firstOccurrence) {
    this.periodInDays = periodInDays;
    this.firstOccurrence = firstOccurrence;
  }

  public int getPeriodInDays() {
    return periodInDays;
  }

  @Override
  public long distanceTo(long timeInMilliseconds) {
    long periodInMillis = DAY_IN_MILLIS * periodInDays;
    long millis = firstOccurrence.getMillis();
    long distanceFromFirstOccurrence = timeInMilliseconds - millis;
    long distanceToTimeInPeriods = distanceFromFirstOccurrence / periodInMillis;
    long distanceToUndershootingOccurrence = timeInMilliseconds - (millis + distanceToTimeInPeriods * periodInMillis);
    boolean isTimeBeforeFirstOccurrence = distanceFromFirstOccurrence < 0;
    long distanceToOvershootingOccurrence = timeInMilliseconds - (millis + (distanceToTimeInPeriods + (isTimeBeforeFirstOccurrence ? -1 : 1)) * periodInMillis);
    return smallestByAbsoluteValue(distanceToUndershootingOccurrence, distanceToOvershootingOccurrence);
  }

  @Override
  public Occurrences getSubOccurrences(final long fromTimeInMillis, final long toTimeInMillis) {
    return new ConstantPeriodOccurrences(getFirstOccurrenceAfter(fromTimeInMillis), periodInDays * DAY_IN_MILLIS, toTimeInMillis);
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
           "firstOccurrence=" + formatDate(firstOccurrence) +
           ", periodInDays=" + periodInDays +
           '}';
  }

  public DateTime getFirstOccurrence() {
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
