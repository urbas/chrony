package si.urbas.chrony.recurrence;

import static si.urbas.chrony.util.MathUtils.smallestByAbsoluteValue;
import static si.urbas.chrony.util.TimeUtils.*;

public class DailyPeriodRecurrence implements Recurrence {

  private final int periodInDays;
  private final long firstOccurrenceInMillis;

  public DailyPeriodRecurrence(int periodInDays, int year, int month, int dayOfMonth, int hourOfDay, int minutesPastHour) {
    this(periodInDays, toUtcTimeInMillis(year, month, dayOfMonth, hourOfDay, minutesPastHour, 0));
  }

  public DailyPeriodRecurrence(int periodInDays, long firstOccurrenceInMillis) {
    this.periodInDays = periodInDays;
    this.firstOccurrenceInMillis = firstOccurrenceInMillis;
  }

  public int getPeriodInDays() {
    return periodInDays;
  }

  @Override
  public long distanceTo(long timeInMilliseconds) {
    long periodInMillis = DAY_IN_MILLIS * periodInDays;
    long distanceFromFirstOccurrence = timeInMilliseconds - firstOccurrenceInMillis;
    long distanceToTimeInPeriods = distanceFromFirstOccurrence / periodInMillis;
    long distanceToUndershootingOccurrence = timeInMilliseconds - (firstOccurrenceInMillis + distanceToTimeInPeriods * periodInMillis);
    boolean isTimeBeforeFirstOccurrence = distanceFromFirstOccurrence < 0;
    long distanceToOvershootingOccurrence = timeInMilliseconds - (firstOccurrenceInMillis + (distanceToTimeInPeriods + (isTimeBeforeFirstOccurrence ? -1 : 1)) * periodInMillis);
    return smallestByAbsoluteValue(distanceToUndershootingOccurrence, distanceToOvershootingOccurrence);
  }

  @Override
  public OccurrenceList getOccurrencesBetween(final long fromTimeInMillis, final long toTimeInMillis) {
    return new RegularOccurrenceList(periodInDays * DAY_IN_MILLIS, getFirstOccurrenceAfter(fromTimeInMillis), toTimeInMillis);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (o == null || getClass() != o.getClass()) { return false; }

    DailyPeriodRecurrence that = (DailyPeriodRecurrence) o;

    return firstOccurrenceInMillis == that.firstOccurrenceInMillis && periodInDays == that.periodInDays;
  }

  @Override
  public int hashCode() {
    int result = periodInDays;
    result = 31 * result + (int) (firstOccurrenceInMillis ^ (firstOccurrenceInMillis >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "DailyPeriodRecurrence{" +
           "firstOccurrence=" + toSimpleString(firstOccurrenceInMillis) +
           ", periodInDays=" + periodInDays +
           '}';
  }

  public long getFirstOccurrenceInMillis() {
    return firstOccurrenceInMillis;
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
