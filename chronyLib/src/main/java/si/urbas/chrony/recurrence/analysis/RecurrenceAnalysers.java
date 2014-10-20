package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;

import java.util.List;

public class RecurrenceAnalysers {
  public static GeneticRecurrenceAnalyser create(List<EventSample> eventSamples) {
    return new GeneticRecurrenceAnalyser(eventSamples, new GuessingRecurrenceAnalyser(eventSamples));
  }
}
