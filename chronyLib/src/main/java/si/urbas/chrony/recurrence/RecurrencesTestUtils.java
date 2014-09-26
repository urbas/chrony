package si.urbas.chrony.recurrence;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecurrencesTestUtils {
  public static Recurrences emptyRecurrences() {
    return new RecurrencesList(Collections.<Recurrence>emptyList());
  }

  public static Recurrences recurrences(Recurrence... recurrences) {
    return new RecurrencesList(recurrences);
  }

  public static List<Recurrence> toList(Recurrence... recurrences) {return Arrays.asList(recurrences);}
}
