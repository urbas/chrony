package si.urbas.chrony.analysis;

import si.urbas.chrony.recurrence.Recurrence;

import java.util.ArrayList;
import java.util.List;

public class GeneticRecurrenceAnalyser implements RecurrenceAnalyser {

  public GeneticRecurrenceAnalyser() {

  }

  @Override
  public List<Recurrence> foundPatterns() {
    return new ArrayList<Recurrence>();
  }

}
