package si.urbas.chrony.analysis;

import si.urbas.chrony.EventSample;

import java.util.List;

public class GeneticRecurrenceAnalyserTest extends RecurrenceAnalyserTest {

  @Override
  protected RecurrenceAnalyser createRecurrenceAnalyser(List<EventSample> eventSamples) {
    return new GeneticRecurrenceAnalyser();
  }

}