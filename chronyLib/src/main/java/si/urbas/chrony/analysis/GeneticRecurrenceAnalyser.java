package si.urbas.chrony.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.ArrayList;
import java.util.List;

public class GeneticRecurrenceAnalyser implements RecurrenceAnalyser {

  public GeneticRecurrenceAnalyser(List<EventSample> eventSamples) {

  }

  @Override
  public List<Recurrence> foundRecurrences() {
    return new ArrayList<Recurrence>();
  }

}
