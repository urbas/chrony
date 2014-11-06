package si.urbas.chrony.recurrence;

import java.util.List;

public interface Occurrences extends List<Long> {
  long getOccurrenceAt(int index);

  int indexOf(long timeInMillis);

  int indexOfClosest(long timeInMillis);
}
