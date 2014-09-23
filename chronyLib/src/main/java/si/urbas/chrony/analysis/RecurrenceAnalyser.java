package si.urbas.chrony.analysis;

import si.urbas.chrony.recurrence.Recurrence;

import java.util.List;

public interface RecurrenceAnalyser {
  List<Recurrence> foundPatterns();
}
