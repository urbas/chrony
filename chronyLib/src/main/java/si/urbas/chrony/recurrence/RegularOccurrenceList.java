package si.urbas.chrony.recurrence;

import java.util.AbstractList;

public class RegularOccurrenceList extends AbstractList<Long> implements OccurrenceList {

  private final long firstOccurrenceInMillis;
  private final long periodInMillis;
  private final long untilTimeInMillis;

  public RegularOccurrenceList(long periodInMillis, long firstOccurrenceInMillis, long untilTimeInMillis) {
    if (periodInMillis <= 0) {
      throw new IllegalArgumentException("The period must be a positive number.");
    }
    this.untilTimeInMillis = untilTimeInMillis;
    this.firstOccurrenceInMillis = firstOccurrenceInMillis;
    this.periodInMillis = periodInMillis;
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

  @Override
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
  public int indexOfClosest(long timeInMillis) {
    return Math.min(size() - 1, Math.max(0, (int) Math.round((double) (timeInMillis - firstOccurrenceInMillis) / periodInMillis)));
  }

  @Override
  public int size() {
    long timeSpanInMillis = untilTimeInMillis - firstOccurrenceInMillis;
    return timeSpanInMillis < 0 ? 0 : (int) (timeSpanInMillis / periodInMillis + 1);
  }
}
