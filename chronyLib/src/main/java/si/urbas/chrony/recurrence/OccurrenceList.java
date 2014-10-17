package si.urbas.chrony.recurrence;

import java.util.AbstractList;

public class OccurrenceList extends AbstractList<Long> {

  private final long firstOccurrenceInMillis;
  private final long periodInMillis;
  private final long toTimeInMillis;

  public OccurrenceList(long firstOccurrenceInMillis, long toTimeInMillis, long periodInMillis) {
    if (periodInMillis <= 0) {
      throw new IllegalArgumentException("The period must be a positive number.");
    }
    this.toTimeInMillis = toTimeInMillis;
    this.firstOccurrenceInMillis = firstOccurrenceInMillis;
    this.periodInMillis = periodInMillis;
  }

  @Override
  public Long get(int index) {
    return getOccurrenceAt(index);
  }

  public long getOccurrenceAt(int index) {
    if (index < 0 || index >= size()) {
      throw new IndexOutOfBoundsException();
    }
    return firstOccurrenceInMillis + index * periodInMillis;
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

  public int indexOf(long timeInMillis) {
    long timeSpanToTimeInMillis = timeInMillis - firstOccurrenceInMillis;
    if (timeSpanToTimeInMillis < 0) {
      return -1;
    } else {
      long periodsToSoughtOccurrence = timeSpanToTimeInMillis / periodInMillis;
      return (periodsToSoughtOccurrence * periodInMillis == timeSpanToTimeInMillis) ? (int) periodsToSoughtOccurrence : -1;
    }
  }

  @Override
  public int size() {
    long timeSpanInMillis = toTimeInMillis - firstOccurrenceInMillis;
    return timeSpanInMillis < 0 ? 0 : (int) (timeSpanInMillis / periodInMillis + 1);
  }
}
