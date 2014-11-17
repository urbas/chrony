package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.List;

public class RecurrenceFinders {

  public static List<Recurrence> findRecurrences(List<EventSample> eventSamples) {
    return new SingleDailyRecurrenceFinder().foundRecurrences(eventSamples);
  }
}
