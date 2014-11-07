package si.urbas.chrony.recurrence;

import java.util.AbstractList;

import static java.lang.Math.*;

public class ConstantPeriodOccurrences extends AbstractList<Long> implements Occurrences, Recurrence {

  public static final ConstantPeriodOccurrences EMPTY_OCCURRENCES = new ConstantPeriodOccurrences(0, 1, -1);
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
  public ConstantPeriodOccurrences getSubOccurrences(long fromTimeInMillis, long untilTimeInMillis) {
    if (fromTimeInMillis <= this.fromTimeInMillis && untilTimeInMillis >= this.untilTimeInMillis) {
      return this;
    } else if (fromTimeInMillis <= this.fromTimeInMillis) {
      return new ConstantPeriodOccurrences(this.fromTimeInMillis, periodInMillis, untilTimeInMillis);
    } else if (untilTimeInMillis >= this.untilTimeInMillis) {
      return new ConstantPeriodOccurrences(fromTimeInMillis, periodInMillis, this.untilTimeInMillis);
    }
    return new ConstantPeriodOccurrences(fromTimeInMillis, periodInMillis, untilTimeInMillis);
  }

  @Override
  public boolean contains(Object o) {
    return o instanceof Long && contains(((Long) o).longValue());
  }

  public boolean contains(long occurrence) {
    return indexOf(occurrence) >= 0;
  }

  @Override
  public ConstantPeriodOccurrences subList(int fromIndex, int toIndex) {
    if (fromIndex == 0 && toIndex == size()) {
      return this;
    } else if (fromIndex >= toIndex) {
      return EMPTY_OCCURRENCES;
    } else {
      return new ConstantPeriodOccurrences(getOccurrenceAt(fromIndex), periodInMillis, getOccurrenceAt(toIndex - 1));
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (o == null) {
      return false;
    } else if (getClass() == o.getClass()) {
      ConstantPeriodOccurrences other = (ConstantPeriodOccurrences) o;
      int size = size();
      if (size == other.size()) {
        if (fromTimeInMillis == other.fromTimeInMillis) {
          return periodInMillis == other.periodInMillis || size <= 1;
        } else {
          return size == 0;
        }
      } else {
        return false;
      }
    } else {
      return super.equals(o);
    }
  }

  @Override
  public int hashCode() {
    int size = size();
    if (size == 0) {
      return 1;
    } else {
      long lastElement = getOccurrenceAt(size - 1);
      int result = 31 * Math.max(size, 1);
      result = 31 * result + (int) (fromTimeInMillis ^ (fromTimeInMillis >>> 32));
      result = 31 * result + (int) (lastElement ^ (lastElement >>> 32));
      return result;
    }
  }
}
