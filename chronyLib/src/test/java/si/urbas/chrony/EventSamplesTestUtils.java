package si.urbas.chrony;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static si.urbas.chrony.util.TimeUtils.createUtcCalendar;

public class EventSamplesTestUtils {

  public static final String EVENT_NAME = "event name";
  public static final int HOUR_17 = 16;
  public static final int DAY_1 = 0;
  public static final int DAY_2 = 1;
  public static final int DAY_3 = 2;
  public static final int DAY_8 = 7;
  public static final int DAY_10 = 9;

  public static List<EventSample> emptyEventSamples() {return Collections.emptyList();}

  public static EventSample eventSampleAtTime(int day, int hour) {
    Calendar calendar = createUtcCalendar();
    calendar.setTimeInMillis(0);
    calendar.add(Calendar.HOUR, day * 24 + hour);
    System.out.println(calendar.getTime());
    return new EventSample(EVENT_NAME, calendar.getTimeInMillis(), null);
  }

}
