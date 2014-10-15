package si.urbas.chrony;

import si.urbas.chrony.util.EventSampleOldestFirstOrder;

import java.util.*;

import static si.urbas.chrony.util.MathUtils.randomValueBetween;
import static si.urbas.chrony.util.TimeUtils.*;

public class EventSamplesTestUtils {

  private static final String EVENT_NAME = "event name";
  public static final int HOUR_17 = 16;
  public static final int DAY_1 = 0;
  public static final int DAY_2 = 1;
  public static final int DAY_3 = 2;
  public static final int DAY_8 = 7;
  public static final int DAY_10 = 9;

  private static EventSample eventSampleAtTime(Calendar calendar) {
    return eventSampleAtTime(calendar.getTimeInMillis());
  }

  private static EventSample eventSampleAtTime(long timeInMillis) {
    return new EventSample(EVENT_NAME, timeInMillis, null);
  }

  public static List<EventSample> emptyEventSamples() {return Collections.emptyList();}

  public static EventSample eventSampleAtTime(int day, int hour) {
    Calendar calendar = createUtcCalendar();
    calendar.set(0, Calendar.JANUARY, day, hour, 0, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return new EventSample(EVENT_NAME, calendar.getTimeInMillis(), null);
  }

  public static ArrayList<EventSample> createRecurringEventSamples(int periodInDays, int durationInDays, Calendar timeOfFirstOccurrence) {
    Calendar calendar = (Calendar) timeOfFirstOccurrence.clone();
    ArrayList<EventSample> eventSamples = new ArrayList<EventSample>();
    do {
      eventSamples.add(EventSamplesTestUtils.eventSampleAtTime(calendar));
      calendar.add(Calendar.DAY_OF_MONTH, periodInDays);
      durationInDays -= periodInDays;
    } while (durationInDays >= 0);
    return eventSamples;
  }

  public static ArrayList<EventSample> createRandomEventSamples(Random randomnessSource, int periodInDays, int durationInDays, int maxDeviationInHours, int year, int month, int dayOfMonth, int hourOfDay, int minutesPastHour) {
    long startTimeInMillis = toUtcTimeInMillis(year, month, dayOfMonth, hourOfDay, minutesPastHour, 0);
    return createRandomEventSamples(randomnessSource, periodInDays, durationInDays, maxDeviationInHours, startTimeInMillis);
  }

  public static ArrayList<EventSample> createRandomEventSamples(Random randomnessSource, int periodInDays, int durationInDays, int maxDeviationInHours, long startTimeInMillis) {
    ArrayList<EventSample> roughlyRecurringSamples = new ArrayList<EventSample>();
    return addRandomEventSamples(roughlyRecurringSamples, randomnessSource, periodInDays, durationInDays, maxDeviationInHours, startTimeInMillis);
  }

  public static ArrayList<EventSample> addRandomEventSamples(ArrayList<EventSample> eventSamplesToAddTo, Random randomnessSource, int periodInDays, int durationInDays, int maxDeviationInHours, long startTimeInMillis) {
    long endTimeInMillis = startTimeInMillis + durationInDays * DAY_IN_MILLIS;
    long maxDeviationFromExactRecurrence = maxDeviationInHours * HOUR_IN_MILLIS;
    addUniformlyRandomOccurrences(eventSamplesToAddTo, randomnessSource, periodInDays, startTimeInMillis, endTimeInMillis, maxDeviationFromExactRecurrence);
    return eventSamplesToAddTo;
  }

  private static void addUniformlyRandomOccurrences(ArrayList<EventSample> samplesToAddTo, Random randomnessSource, long periodInDays, long startTimeInMillis, long endTimeInMillis, long maxDeviationFromExactRecurrence) {
    long periodInMillis = periodInDays * DAY_IN_MILLIS;
    for (long currentOccurrence = startTimeInMillis; currentOccurrence < endTimeInMillis; currentOccurrence += periodInMillis) {
      long timeInMillis = currentOccurrence + randomValueBetween(randomnessSource, -maxDeviationFromExactRecurrence, maxDeviationFromExactRecurrence);
      samplesToAddTo.add(eventSampleAtTime(timeInMillis));
    }
    Collections.sort(samplesToAddTo, EventSampleOldestFirstOrder.INSTANCE);
  }
}
