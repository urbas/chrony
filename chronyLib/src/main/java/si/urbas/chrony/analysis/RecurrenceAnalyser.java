package si.urbas.chrony.analysis;

import java.util.List;

public abstract class RecurrenceAnalyser {
  public abstract List<PeriodicRecurrencePattern> foundPatterns();
}
