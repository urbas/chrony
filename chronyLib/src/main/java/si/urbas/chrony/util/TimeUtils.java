package si.urbas.chrony.util;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;

public class TimeUtils {

  public static final long TIME_0 = 0L;
  public static final long SECOND_IN_MILLIS = 1000;
  public static final long MINUTE_IN_MILLIS = 60 * SECOND_IN_MILLIS;
  public static final long HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS;
  public static final int DAY_IN_HOURS = 24;
  public static final long DAY_IN_MILLIS = DAY_IN_HOURS * HOUR_IN_MILLIS;
  public static final long WEEK_IN_MILLIS = 7 * DAY_IN_MILLIS;
  private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
  private static final PeriodFormatter PERIOD_FORMATTER = PeriodFormat.getDefault();

  public static String formatPeriod(long periodInMillis) {
    return PERIOD_FORMATTER.print(new Period(0, periodInMillis));
  }

  public static long toUtcTimeInMillis(int year, int month, int dayOfMonth, int hourOfDay, int minutesPastHour, int secondsPastMinute) {
    return toUtcDate(year, month, dayOfMonth, hourOfDay, minutesPastHour, secondsPastMinute).getMillis();
  }

  public static DateTime createUtcDate() {return new DateTime(ISOChronology.getInstanceUTC());}

  public static DateTime toUtcDate(long utcTimeInMillisSinceEpoch) {
    return new DateTime(utcTimeInMillisSinceEpoch, ISOChronology.getInstanceUTC());
  }

  public static DateTime toUtcDate(int year, int month, int dayOfMonth, int hourOfDay, int minutesPastHour, int secondsPastMinute, int millisecondsPastSecond) {
    return new DateTime(year, month, dayOfMonth, hourOfDay, minutesPastHour, secondsPastMinute, millisecondsPastSecond, ISOChronology.getInstanceUTC());
  }

  public static DateTime toUtcDate(int year, int month, int dayOfMonth, int hourOfDay, int minutesPastHour, int secondsPastMinute) {
    return new DateTime(year, month, dayOfMonth, hourOfDay, minutesPastHour, secondsPastMinute, 0, ISOChronology.getInstanceUTC());
  }

  public static String formatDate(DateTime date) {
    return date.toString(DEFAULT_DATE_TIME_FORMATTER);
  }

  public static String formatDate(long utcTimeInMillis) {
    return formatDate(TimeUtils.toUtcDate(utcTimeInMillis));
  }

}
