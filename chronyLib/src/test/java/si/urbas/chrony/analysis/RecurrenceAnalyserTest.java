package si.urbas.chrony.analysis;

import si.urbas.chrony.Event;
import si.urbas.chrony.EventSample;

import java.util.List;

public abstract class RecurrenceAnalyserTest {
  protected abstract DayRecurrenceAnalyser createRecurrenceAnalyser(List<EventSample> eventSamples);
}
