package si.urbas.chrony.app;

import android.content.Context;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import si.urbas.chrony.EventRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventListViewHelpers {

  private static final String EVENT_NAME_FIELD = "eventName";
  private static final String EVENT_COUNT_FIELD = "eventCount";
  private static final String[] FROM_FIELDS = new String[]{EVENT_NAME_FIELD, EVENT_COUNT_FIELD};
  private static final int[] TO_VIEWS = new int[]{R.id.eventNameTextView, R.id.eventCountTextView};

  public static ListAdapter toListAdapter(Context context, EventRepository eventRepository) {
    ArrayList<Map<String, String>> eventListItems = new ArrayList<Map<String, String>>();
    Iterable<String> events = eventRepository.allEvents();
    for (String eventName : events) {
      HashMap<String, String> eventListItem = new HashMap<String, String>();
      eventListItem.put(EVENT_NAME_FIELD, eventName);
      eventListItem.put(EVENT_COUNT_FIELD, Integer.toString(eventRepository.eventCount(eventName)));
      eventListItems.add(eventListItem);
    }
    return new SimpleAdapter(context, eventListItems, R.layout.event_list_item_view, FROM_FIELDS, TO_VIEWS);
  }
}
