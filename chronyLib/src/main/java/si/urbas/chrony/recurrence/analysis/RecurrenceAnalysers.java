package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;

import java.util.List;

public class RecurrenceAnalysers {
  public static GeneticRecurrenceFinder create(List<EventSample> eventSamples) {
    return new GeneticRecurrenceFinder(eventSamples, new GuessingRecurrenceFinder(eventSamples));
  }
}
