package si.urbas.chrony.recurrence;

import java.util.List;

public interface IndexedRecurrence extends List<Long>, Recurrence {
  long getOccurrenceAt(int index);

  int indexOf(long timeInMillis);

  int indexOfClosest(long timeInMillis);
}
