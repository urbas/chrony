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
  private static final Random RANDOMNESS_SOURCE = new Random(4983291827L);

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

  public static ArrayList<EventSample> createRandomEventSamples(int periodInDays, int durationInDays, int maxDeviationInHours, int year, int month, int dayOfMonth, int hourOfDay, int minutesPastHour) {
    long startTimeInMillis = toUtcTimeInMillis(year, month, dayOfMonth, hourOfDay, minutesPastHour, 0);
    return createRandomEventSamples(periodInDays, durationInDays, maxDeviationInHours, startTimeInMillis);
  }

  public static ArrayList<EventSample> createRandomEventSamples(int periodInDays, int durationInDays, int maxDeviationInHours, long startTimeInMillis) {
    ArrayList<EventSample> roughlyRecurringSamples = new ArrayList<EventSample>();
    return addRandomEventSamples(roughlyRecurringSamples, periodInDays, durationInDays, maxDeviationInHours, startTimeInMillis);
  }

  public static ArrayList<EventSample> addRandomEventSamples(ArrayList<EventSample> eventSamplesToAddTo, int periodInDays, int durationInDays, int maxDeviationInHours, long startTimeInMillis) {
    long endTimeInMillis = startTimeInMillis + durationInDays * DAY_IN_MILLIS;
    long maxDeviationFromExactRecurrence = maxDeviationInHours * HOUR_IN_MILLIS;
    addUniformlyRandomOccurrences(eventSamplesToAddTo, RANDOMNESS_SOURCE, periodInDays, startTimeInMillis, endTimeInMillis, maxDeviationFromExactRecurrence);
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
