package si.urbas.chrony.recurrence;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecurrencesList implements Recurrences {
  public static final RecurrencesList emptyRecurrences = new RecurrencesList(Collections.<Recurrence>emptyList());
  private final List<Recurrence> recurrenceList;

  public RecurrencesList(List<Recurrence> recurrenceList) {
    this.recurrenceList = recurrenceList;
  }

  public RecurrencesList(Recurrence... recurrences) {
    this(Arrays.asList(recurrences));
  }

  @Override
  public int getRecurrencesCount() {
    return recurrenceList.size();
  }

  @Override
  public List<Recurrence> getRecurrences() {
    return recurrenceList;
  }
}
