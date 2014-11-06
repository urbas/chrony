package si.urbas.chrony.recurrence;

import java.util.AbstractList;

import static java.lang.Math.*;

public class ConstantPeriodOccurrences extends AbstractList<Long> implements Occurrences, Recurrence {

  private final long fromTimeInMillis;
  private final long periodInMillis;
  private final long untilTimeInMillis;

  public ConstantPeriodOccurrences(long fromTimeInMillis, long periodInMillis, long untilTimeInMillis) {
    if (periodInMillis <= 0) {
      throw new IllegalArgumentException("The period must be a positive number.");
    }
    this.untilTimeInMillis = untilTimeInMillis;
    this.fromTimeInMillis = fromTimeInMillis;
    this.periodInMillis = periodInMillis;
  }

  public long getFromTimeInMillis() {
    return fromTimeInMillis;
  }

  public long getPeriodInMillis() {
    return periodInMillis;
  }

  public long getUntilTimeInMillis() {
    return untilTimeInMillis;
  }

  @Override
  public Long get(int index) {
    return getOccurrenceAt(index);
  }

  @Override
  public long getOccurrenceAt(int index) {
    if (index < 0 || index >= size()) {
      throw new IndexOutOfBoundsException();
    }
    return fromTimeInMillis + index * periodInMillis;
  }

  @Override
  public int indexOf(Object o) {
    return o instanceof Long ? indexOf((Long) o) : -1;
  }

  @Override
  public int lastIndexOf(Object o) {
    return indexOf(o);
  }

  public int indexOf(Long timeInMillis) {
    return indexOf(timeInMillis.longValue());
  }

  @Override
  public int indexOf(long timeInMillis) {
    long timeSpanToTimeInMillis = timeInMillis - fromTimeInMillis;
    if (timeSpanToTimeInMillis < 0) {
      return -1;
    } else {
      long periodsToSoughtOccurrence = timeSpanToTimeInMillis / periodInMillis;
      return (periodsToSoughtOccurrence * periodInMillis == timeSpanToTimeInMillis) ? (int) periodsToSoughtOccurrence : -1;
    }
  }

  @Override
  public int indexOfClosest(long timeInMillis) {
    return min(size() - 1, max(0, (int) round((double) (timeInMillis - fromTimeInMillis) / periodInMillis)));
  }

  @Override
  public int size() {
    long timeSpanInMillis = untilTimeInMillis - fromTimeInMillis;
    return timeSpanInMillis < 0 ? 0 : (int) (timeSpanInMillis / periodInMillis + 1);
  }

  @Override
  public long distanceTo(long timeInMilliseconds) {
    return timeInMilliseconds - getOccurrenceAt(indexOfClosest(timeInMilliseconds));
  }

  @Override
  public Occurrences getSubOccurrences(long fromTimeInMillis, long untilTimeInMillis) {
    return new ConstantPeriodOccurrences(
      max(fromTimeInMillis, this.fromTimeInMillis),
      periodInMillis,
      min(untilTimeInMillis, this.untilTimeInMillis)
    );
  }

  @Override
  public boolean contains(Object o) {
    return o instanceof Long && contains(((Long) o).longValue());
  }

  public boolean contains(long occurrence) {
    return indexOf(occurrence) >= 0;
  }
}
