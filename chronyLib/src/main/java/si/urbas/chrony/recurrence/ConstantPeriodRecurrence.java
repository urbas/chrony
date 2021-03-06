package si.urbas.chrony.recurrence;

import java.util.AbstractList;
import java.util.RandomAccess;

import static java.lang.Math.*;
import static si.urbas.chrony.util.TimeUtils.formatDate;
import static si.urbas.chrony.util.TimeUtils.formatPeriod;

public class ConstantPeriodRecurrence extends AbstractList<Long> implements PeriodicRecurrence, IndexedRecurrence, RandomAccess {

  public static final ConstantPeriodRecurrence EMPTY_OCCURRENCES = new ConstantPeriodRecurrence(0, 1, -1);
  private final long fromTimeInMillis;
  private final long periodInMillis;
  private final long untilTimeInMillis;

  public ConstantPeriodRecurrence(long fromTimeInMillis, long periodInMillis, long untilTimeInMillis) {
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

  @Override
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
  public ConstantPeriodRecurrence subOccurrences(long fromTimeInMillis, long untilTimeInMillis) {
    if (fromTimeInMillis <= this.fromTimeInMillis && untilTimeInMillis >= this.untilTimeInMillis) {
      return this;
    } else if (fromTimeInMillis <= this.fromTimeInMillis) {
      return new ConstantPeriodRecurrence(this.fromTimeInMillis, periodInMillis, untilTimeInMillis);
    } else if (untilTimeInMillis >= this.untilTimeInMillis) {
      return new ConstantPeriodRecurrence(fromTimeInMillis, periodInMillis, this.untilTimeInMillis);
    }
    return new ConstantPeriodRecurrence(fromTimeInMillis, periodInMillis, untilTimeInMillis);
  }

  @Override
  public boolean contains(Object o) {
    return o instanceof Long && contains(((Long) o).longValue());
  }

  public boolean contains(long occurrence) {
    return indexOf(occurrence) >= 0;
  }

  @Override
  public ConstantPeriodRecurrence subList(int fromIndex, int toIndex) {
    if (fromIndex == 0 && toIndex == size()) {
      return this;
    } else if (fromIndex >= toIndex) {
      return EMPTY_OCCURRENCES;
    } else {
      return new ConstantPeriodRecurrence(getOccurrenceAt(fromIndex), periodInMillis, getOccurrenceAt(toIndex - 1));
    }
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    } else if (other == null) {
      return false;
    } else if (getClass() == other.getClass()) {
      return equals((ConstantPeriodRecurrence) other);
    } else {
      return super.equals(other);
    }
  }

  public boolean equals(ConstantPeriodRecurrence other) {
    int size = size();
    if (size != other.size()) {
      return false;
    }
    if (fromTimeInMillis == other.fromTimeInMillis) {
      return periodInMillis == other.periodInMillis || size <= 1;
    } else {
      return size == 0;
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

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ConstantPeriodRecurrence{")
                 .append("fromTimeInMillis=").append(formatDate(fromTimeInMillis))
                 .append(", periodInMillis=").append(formatPeriod(periodInMillis))
                 .append(", untilTimeInMillis=").append(formatDate(untilTimeInMillis));
    stringBuilder.append(", occurrences=");
    printOccurrences(stringBuilder);
    stringBuilder.append("}");
    return stringBuilder.toString();
  }

  private void printOccurrences(StringBuilder sb) {
    sb.append("[");
    if (!isEmpty()) {
      sb.append(formatDate(get(0)));
      for (int i = 1; i < size(); i++) {
        sb.append(", ").append(formatDate(get(i)));
      }
    }
    sb.append("]");
  }
}
