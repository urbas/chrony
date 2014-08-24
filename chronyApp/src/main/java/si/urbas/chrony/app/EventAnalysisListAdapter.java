package si.urbas.chrony.app;

import android.content.Context;
import android.widget.SimpleAdapter;
import si.urbas.chrony.AnalysedEvent;
import si.urbas.chrony.Analysis;

import java.util.*;

public class EventAnalysisListAdapter extends SimpleAdapter {

  private static final String EVENT_NAME_FIELD = "eventName";
  private static final String EVENT_COUNT_FIELD = "eventCount";
  private static final String[] FROM_FIELDS = new String[]{EVENT_NAME_FIELD, EVENT_COUNT_FIELD};
  private static final int[] TO_VIEWS = new int[]{R.id.eventNameTextView, R.id.eventCountTextView};
  private final List<Map<String, String>> listViewItems;

  public EventAnalysisListAdapter(Context context, Analysis analysis) {
    this(context, toSimpleForm(analysis));
  }

  private EventAnalysisListAdapter(Context context, List<Map<String, String>> listViewItems) {
    super(context, listViewItems, R.layout.event_list_item_view, FROM_FIELDS, TO_VIEWS);
    this.listViewItems = listViewItems;
  }

  public String getEventName(int i) {
    return listViewItems.get(i).get(EVENT_NAME_FIELD);
  }

  private static List<Map<String, String>> toSimpleForm(Analysis analysis) {
    ArrayList<Map<String, String>> eventListItems = new ArrayList<Map<String, String>>();
    List<AnalysedEvent> analysedEvents = analysis.getAnalysedEvents();
    for (AnalysedEvent analysedEvent : analysedEvents) {
      HashMap<String, String> eventListItem = new HashMap<String, String>();
      eventListItem.put(EVENT_NAME_FIELD, analysedEvent.getEventName());
      eventListItem.put(EVENT_COUNT_FIELD, Integer.toString(analysedEvent.getCount()));
      eventListItems.add(eventListItem);
    }
    return eventListItems;
  }

}
