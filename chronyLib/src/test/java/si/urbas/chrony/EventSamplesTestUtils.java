package si.urbas.chrony;

import org.joda.time.DateTime;
import si.urbas.chrony.util.EventSampleOldestFirstComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static si.urbas.chrony.util.MathUtils.randomValueBetween;
import static si.urbas.chrony.util.TimeUtils.*;

public class EventSamplesTestUtils {

  private static final String EVENT_NAME = "event name";
  public static final int DAY_1 = 1;
  public static final int DAY_2 = 2;
  public static final int DAY_3 = 3;
  public static final int DAY_8 = 8;
  public static final int DAY_10 = 10;
  public static final int HOUR_17 = 17;

  public static EventSample eventSampleAt(DateTime timestamp) {
    return new EventSample(EVENT_NAME, timestamp, null);
  }

  public static EventSample eventSampleAt(long timeInMillis) {
    return new EventSample(EVENT_NAME, timeInMillis, null);
  }

  public static List<EventSample> emptyEventSamples() {return Collections.emptyList();}

  public static EventSample eventSampleAtTime(int day, int hour) {
    return new EventSample(EVENT_NAME, toUtcDate(0, 1, day, hour, 0, 0), null);
  }

  public static ArrayList<EventSample> createRecurringEventSamples(int periodInDays, int durationInDays, long timeOfFirstOccurrence) {
    return createRecurringEventSamples(periodInDays, durationInDays, toUtcDate(timeOfFirstOccurrence));
  }

  public static ArrayList<EventSample> createRecurringEventSamples(int periodInDays, int durationInDays, DateTime timeOfFirstOccurrence) {
    ArrayList<EventSample> eventSamples = new ArrayList<EventSample>();
    do {
      eventSamples.add(EventSamplesTestUtils.eventSampleAt(timeOfFirstOccurrence));
      timeOfFirstOccurrence = timeOfFirstOccurrence.plusDays(periodInDays);
      durationInDays -= periodInDays;
    } while (durationInDays >= 0);
    return eventSamples;
  }

  public static ArrayList<EventSample> createRandomEventSamples(Random randomnessSource, int periodInDays, int durationInDays, long maxDeviationInMillis, int year, int month, int dayOfMonth, int hourOfDay, int minutesPastHour) {
    long startTimeInMillis = toUtcTimeInMillis(year, month, dayOfMonth, hourOfDay, minutesPastHour, 0);
    return createRandomEventSamples(randomnessSource, periodInDays, durationInDays, maxDeviationInMillis, startTimeInMillis);
  }

  public static ArrayList<EventSample> createRandomEventSamples(Random randomnessSource, int periodInDays, int durationInDays, long maxDeviationInMillis, long startTimeInMillis) {
    ArrayList<EventSample> roughlyRecurringSamples = new ArrayList<EventSample>();
    return addRandomEventSamples(roughlyRecurringSamples, randomnessSource, periodInDays, durationInDays, maxDeviationInMillis, startTimeInMillis);
  }

  public static ArrayList<EventSample> addRandomEventSamples(ArrayList<EventSample> eventSamplesToAddTo, Random randomnessSource, int periodInDays, int durationInDays, long maxDeviationInMillis, long startTimeInMillis) {
    long endTimeInMillis = startTimeInMillis + durationInDays * DAY_IN_MILLIS;
    addUniformlyRandomOccurrences(eventSamplesToAddTo, randomnessSource, periodInDays, startTimeInMillis, endTimeInMillis, maxDeviationInMillis);
    return eventSamplesToAddTo;
  }

  private static void addUniformlyRandomOccurrences(ArrayList<EventSample> samplesToAddTo, Random randomnessSource, long periodInDays, long startTimeInMillis, long endTimeInMillis, long maxDeviationInMillis) {
    long periodInMillis = periodInDays * DAY_IN_MILLIS;
    for (long currentOccurrence = startTimeInMillis; currentOccurrence <= endTimeInMillis; currentOccurrence += periodInMillis) {
      long timeInMillis = currentOccurrence + randomValueBetween(randomnessSource, -maxDeviationInMillis, maxDeviationInMillis);
      samplesToAddTo.add(eventSampleAt(timeInMillis));
    }
    Collections.sort(samplesToAddTo, EventSampleOldestFirstComparator.INSTANCE);
  }
}
