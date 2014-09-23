package si.urbas.chrony.analysis;

import si.urbas.chrony.recurrence.Recurrence;

import java.util.List;

public abstract class RecurrenceAnalyser {
  public abstract List<Recurrence> foundPatterns();
}
