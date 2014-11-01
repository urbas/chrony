package si.urbas.chrony.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {

  public static final long TIME_0 = 0L;
  public static final long SECOND_IN_MILLIS = 1000;
  public static final long MINUTE_IN_MILLIS = 60 * SECOND_IN_MILLIS;
  public static final long HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS;
  public static final int DAY_IN_HOURS = 24;
  public static final long DAY_IN_MILLIS = DAY_IN_HOURS * HOUR_IN_MILLIS;
  public static final long WEEK_IN_MILLIS = 7 * DAY_IN_MILLIS;
  private static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
  private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  public static long toUtcTimeInMillis(int year, int month, int dayOfMonth, int hourOfDay, int minutesPastHour, int secondsPastMinute) {
    return toUtcCalendar(year, month, dayOfMonth, hourOfDay, minutesPastHour, secondsPastMinute).getTimeInMillis();
  }

  public static Calendar createUtcCalendar() {return Calendar.getInstance(UTC_TIME_ZONE);}

  public static Calendar toUtcCalendar(long utcTimeInMillisSinceEpoch) {
    Calendar calendar = createUtcCalendar();
    calendar.setTimeInMillis(utcTimeInMillisSinceEpoch);
    return calendar;
  }

  public static Calendar toUtcCalendar(int year, int month, int dayOfMonth, int hourOfDay, int minutesPastHour, int secondsPastMinute, int millisecondsPastSecond) {
    Calendar calendar = createUtcCalendar();
    calendar.set(year, month, dayOfMonth, hourOfDay, minutesPastHour, secondsPastMinute);
    calendar.set(Calendar.MILLISECOND, millisecondsPastSecond);
    return calendar;
  }

  public static Calendar toUtcCalendar(int year, int month, int dayOfMonth, int hourOfDay, int minutesPastHour, int secondsPastMinute) {
    return toUtcCalendar(year, month, dayOfMonth, hourOfDay, minutesPastHour, secondsPastMinute, 0);
  }

  public static String toSimpleString(Calendar calendar) {
    return SIMPLE_DATE_FORMAT.format(calendar.getTime());
  }

  public static String toSimpleString(long utcTimeInMillis) {
    return SIMPLE_DATE_FORMAT.format(new Date(utcTimeInMillis));
  }

  public static long timeOfDayInMillis(Calendar calendar) {
    return calendar.get(Calendar.HOUR_OF_DAY) * HOUR_IN_MILLIS +
           calendar.get(Calendar.MINUTE) * MINUTE_IN_MILLIS +
           calendar.get(Calendar.SECOND) * SECOND_IN_MILLIS +
           calendar.get(Calendar.MILLISECOND);
  }

  /**
   * @return a calendar with time of day set to 00:00:00.000.
   */
  public static Calendar withDateOnly(Calendar calendar) {
    Calendar newCalendar = (Calendar) calendar.clone();
    newCalendar.set(Calendar.MILLISECOND, 0);
    newCalendar.set(Calendar.SECOND, 0);
    newCalendar.set(Calendar.MINUTE, 0);
    newCalendar.set(Calendar.HOUR_OF_DAY, 0);
    return newCalendar;
  }

  public static Calendar withTimeOfDay(Calendar calendar, long timeOfDayInMillis) {
    Calendar newCalendar = (Calendar) calendar.clone();
    int hourOfDay = (int) (timeOfDayInMillis / HOUR_IN_MILLIS);
    long remainingTimeOfDayInMillis = timeOfDayInMillis - hourOfDay * HOUR_IN_MILLIS;
    newCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
    int minutes = (int) (remainingTimeOfDayInMillis / MINUTE_IN_MILLIS);
    remainingTimeOfDayInMillis = remainingTimeOfDayInMillis - minutes * MINUTE_IN_MILLIS;
    newCalendar.set(Calendar.MINUTE, minutes);
    int seconds = (int) (remainingTimeOfDayInMillis / SECOND_IN_MILLIS);
    remainingTimeOfDayInMillis = remainingTimeOfDayInMillis - seconds * SECOND_IN_MILLIS;
    newCalendar.set(Calendar.SECOND, seconds);
    newCalendar.set(Calendar.MILLISECOND, (int) remainingTimeOfDayInMillis);
    return newCalendar;
  }
}
