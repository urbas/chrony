package si.urbas.chrony.app;

import android.content.Context;
import android.widget.SimpleAdapter;
import si.urbas.chrony.EventRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventListSimpleAdapter extends SimpleAdapter {

  private static final String EVENT_NAME_FIELD = "eventName";
  private static final String EVENT_COUNT_FIELD = "eventCount";
  private static final String[] FROM_FIELDS = new String[]{EVENT_NAME_FIELD, EVENT_COUNT_FIELD};
  private static final int[] TO_VIEWS = new int[]{R.id.eventNameTextView, R.id.eventCountTextView};
  private final List<Map<String, String>> eventRepository;

  public EventListSimpleAdapter(Context context, EventRepository eventRepository) {
    this(context, toSimpleForm(eventRepository));
  }

  public EventListSimpleAdapter(Context context, List<Map<String, String>> eventRepository) {
    super(context, eventRepository, R.layout.event_list_item_view, FROM_FIELDS, TO_VIEWS);
    this.eventRepository = eventRepository;
  }

  public String getEventName(int i) {
    return eventRepository.get(i).get(EVENT_NAME_FIELD);
  }

  private static List<Map<String, String>> toSimpleForm(EventRepository eventRepository) {
    ArrayList<Map<String, String>> eventListItems = new ArrayList<Map<String, String>>();
    Iterable<String> events = eventRepository.allEvents();
    for (String eventName : events) {
      HashMap<String, String> eventListItem = new HashMap<String, String>();
      eventListItem.put(EVENT_NAME_FIELD, eventName);
      eventListItem.put(EVENT_COUNT_FIELD, Integer.toString(eventRepository.eventCount(eventName)));
      eventListItems.add(eventListItem);
    }
    return eventListItems;
  }
}
