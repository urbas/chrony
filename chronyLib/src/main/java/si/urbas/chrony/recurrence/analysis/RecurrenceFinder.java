package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.List;

public interface RecurrenceFinder {
  List<Recurrence> foundRecurrences(List<EventSample> eventSamples);
}
