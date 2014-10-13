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

  public static Calendar toUtcCalendar(int year, int month, int dayOfMonth, int hourOfDay, int minutesPastHour, int secondsPastMinute) {
    Calendar calendar = createUtcCalendar();
    calendar.set(year, month, dayOfMonth, hourOfDay, minutesPastHour, secondsPastMinute);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar;
  }

  public static String toSimpleString(Calendar calendar) {
    return SIMPLE_DATE_FORMAT.format(calendar.getTime());
  }

  public static String toSimpleString(long utcTimeInMillis) {
    return SIMPLE_DATE_FORMAT.format(new Date(utcTimeInMillis));
  }
}
