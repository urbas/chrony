package si.urbas.chrony.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.List;

public class GeneticRecurrenceAnalyser implements RecurrenceAnalyser {

  public GeneticRecurrenceAnalyser(List<EventSample> eventSamples) {

  }

  @Override
  public List<Recurrence> foundPatterns() {
    return null;
  }

  public static List<Recurrence> analyse(List<EventSample> eventSamples) {
    return new GeneticRecurrenceAnalyser(eventSamples).foundPatterns();
  }
}
