package si.urbas.chrony.recurrence.analysis;

import si.urbas.chrony.EventSample;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.recurrence.Recurrences;

import java.util.List;

public class RecurrenceFitnessPolicy {

  private static final int SIZE_PENALTY_RATE = -1000;
  private final List<EventSample> eventSamples;

  public RecurrenceFitnessPolicy(List<EventSample> eventSamples) {
    this.eventSamples = eventSamples;
  }

  public double fitness(Recurrences recurrences) {
    return sizePenalty(recurrences) + timeMismatchPenalty(recurrences);
  }

  private int timeMismatchPenalty(Recurrences recurrences) {
    int penalty = 0;
    for (EventSample eventSample : eventSamples) {
      penalty -= findMinimumTimeDifference(recurrences, eventSample);
    }
    return penalty;
  }

  /**
   * @return difference in milliseconds between the closest recurrence and the timestamp of this event sample.
   */
  private long findMinimumTimeDifference(Recurrences recurrences, EventSample eventSample) {
    long minimumTimeDifference = 0;
    for (Recurrence recurrence : recurrences.getRecurrences()) {
      long currentTimeDifference = recurrence.differenceTo(eventSample.getTimestamp());
      if (currentTimeDifference >= minimumTimeDifference) {
        minimumTimeDifference = currentTimeDifference;
      }
    }
    return minimumTimeDifference;
  }

  private static int sizePenalty(Recurrences recurrences) {
    return recurrences.getRecurrencesCount() * SIZE_PENALTY_RATE;
  }
}
