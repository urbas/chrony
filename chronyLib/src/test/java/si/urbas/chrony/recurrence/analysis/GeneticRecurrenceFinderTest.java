package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;

import java.util.List;

public class GeneticRecurrenceFinderTest extends RecurrenceFinderTest {

  @Override
  protected RecurrenceFinder createRecurrenceAnalyser(List<EventSample> eventSamples) {
    return new GeneticRecurrenceFinder(eventSamples, new GuessingRecurrenceFinder(eventSamples));
  }

}