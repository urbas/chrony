package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.Recurrence;

import java.util.Collections;
import java.util.List;

public class GuessingRecurrenceFinder implements RecurrenceFinder {

  @Override
  public List<Recurrence> foundRecurrences(List<EventSample> eventSamples) {
    if (eventSamples.size() > 1) {
      return guessPossibleRecurrences(eventSamples);
    } else {
      return Collections.emptyList();
    }
  }

  private static List<Recurrence> guessPossibleRecurrences(List<EventSample> eventSamples) {
    int[] timesOfDay = new TimeOfDayClusterer().millisOfDayClusters(eventSamples);
    return Collections.emptyList();
  }

}