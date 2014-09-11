package si.urbas.chrony.analysis;

import si.urbas.chrony.EventSample;

import java.util.List;

import static si.urbas.chrony.util.TimeUtils.MILLIS_1_DAY;

public class RecurrenceAnalysis {
  public static RecurrencePattern extractRecurrencePattern(List<EventSample> eventSamples) {
    if (eventSamples.size() > 1) {
      if (Math.abs(eventSamples.get(0).getTimestamp() - eventSamples.get(1).getTimestamp()) > MILLIS_1_DAY) {
        return new WeeklyRecurrencePattern();
      } else {
        return new DailyRecurrencePattern();
      }
    } else {
      return UnknownRecurrencePattern.INSTANCE;
    }
  }
}
