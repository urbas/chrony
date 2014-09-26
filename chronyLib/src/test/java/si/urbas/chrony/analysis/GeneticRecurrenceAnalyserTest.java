package si.urbas.chrony.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.analysis.GeneticRecurrenceAnalyser;
import si.urbas.chrony.recurrence.analysis.RecurrenceAnalyser;

import java.util.List;

public class GeneticRecurrenceAnalyserTest extends RecurrenceAnalyserTest {

  @Override
  protected RecurrenceAnalyser createRecurrenceAnalyser(List<EventSample> eventSamples) {
    return new GeneticRecurrenceAnalyser(eventSamples);
  }

}