package si.urbas.chrony.recurrence;

import java.util.Collections;

public class RecurrencesTestUtils {
  public static Recurrences emptyRecurrences() {
    return new RecurrencesList(Collections.<Recurrence>emptyList());
  }

  public static Recurrences recurrences(Recurrence... recurrences) {
    return new RecurrencesList(recurrences);
  }
}
