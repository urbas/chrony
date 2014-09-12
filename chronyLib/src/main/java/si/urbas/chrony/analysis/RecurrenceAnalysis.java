package si.urbas.chrony.analysis;

import si.urbas.chrony.EventSample;

import java.util.ArrayList;
import java.util.List;

import static si.urbas.chrony.util.TimeUtils.MILLIS_1_DAY;

public class RecurrenceAnalysis {
  public static RecurrencePattern extractRecurrencePattern(List<EventSample> eventSamples) {
    RecurrencePattern foundRecurrencePattern = UnknownRecurrencePattern.INSTANCE;
    ArrayList<EventSample> remainingEventSamples = new ArrayList<EventSample>(eventSamples);

//    if (Math.abs(remainingEventSamples.get(0).getTimestamp() - remainingEventSamples.get(1).getTimestamp()) > MILLIS_1_DAY) {
//      return new WeeklyRecurrencePattern();
//    } else {
//      return new DailyRecurrencePattern();
//    }

    while (remainingEventSamples.size() > 1) {
      RecurrencePattern lastFoundPattern = extractSinglePattern(remainingEventSamples);
      if (foundRecurrencePattern == UnknownRecurrencePattern.INSTANCE) {
        foundRecurrencePattern = lastFoundPattern;
      } else {
        if (foundRecurrencePattern instanceof CompositeRecurrencePattern) {
          ((CompositeRecurrencePattern) foundRecurrencePattern).addSubPattern(lastFoundPattern);
        } else {
          foundRecurrencePattern = new CompositeRecurrencePattern(foundRecurrencePattern, lastFoundPattern);
        }
      }
    }
    return foundRecurrencePattern;
  }

  private static RecurrencePattern extractSinglePattern(ArrayList<EventSample> remainingEventSamples) {
    WeeklyRecurrencePattern weeklyRecurrencePattern = extractWeeklyPattern(remainingEventSamples);
    if (weeklyRecurrencePattern != null) {
      return weeklyRecurrencePattern;
    }

    DailyRecurrencePattern dailyRecurrencePattern = extractDailyRecurrencePattern(remainingEventSamples);
    if (dailyRecurrencePattern != null) {
      return dailyRecurrencePattern;
    }

    remainingEventSamples.clear();
    return null;
  }

  static DailyRecurrencePattern extractDailyRecurrencePattern(ArrayList<EventSample> remainingEventSamples) {
    return null;
  }

  static WeeklyRecurrencePattern extractWeeklyPattern(ArrayList<EventSample> eventSamples) {
    return null;
  }
}
