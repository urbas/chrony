package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.List;

import static java.util.Collections.emptyList;

public class SingleDailyRecurrenceFinder implements RecurrenceFinder {
  public SingleDailyRecurrenceFinder(List<EventSample> eventSamples) {

  }

  @Override
  public List<Recurrence> foundRecurrences() {
    return emptyList();
  }
}
