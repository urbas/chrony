package si.urbas.chrony.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import si.urbas.chrony.AnalysedEvent;
import si.urbas.chrony.Analysis;
import si.urbas.chrony.Event;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.analysis.SimpleAnalyser;

import java.util.Date;
import java.util.List;

public class AnalysedEventsListAdapter extends BaseExpandableListAdapter {

  private final Context context;
  private final SimpleAnalyser analyser;
  private final EventRepository eventRepository;
  private List<AnalysedEvent> analysedEvents;

  public AnalysedEventsListAdapter(Context context, SimpleAnalyser analyser, EventRepository eventRepository) {
    this.context = context;
    this.analyser = analyser;
    this.eventRepository = eventRepository;
    refreshAnalysedEvents();
  }

  @Override
  public int getGroupCount() {
    return analysedEvents.size();
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return 1;
  }

  @Override
  public AnalysedEvent getGroup(int groupPosition) {
    return analysedEvents.get(groupPosition);
  }

  @Override
  public List<Long> getChild(int groupPosition, int childPosition) {
    String eventName = getGroup(groupPosition).getEventName();
    return eventRepository.timestampsOf(eventName);
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = createEventItemView();
    }
    bindEventToItemView(groupPosition, convertView);
    return convertView;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    View childView = convertView == null ? createChildView() : convertView;
    bindTimestampsToView(groupPosition, childView);
    return childView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

  public void addEvent(String eventName) {
    eventRepository.addEvent(new Event(eventName, new Date().getTime()));
    refreshAnalysedEvents();
  }

  public void removeTimestamp(String eventName, Long timestamp) {
    eventRepository.removeTimestamp(eventName, timestamp);
    refreshAnalysedEvents();
  }

  public void clear() {
    eventRepository.clear();
    refreshAnalysedEvents();
  }

  private void refreshAnalysedEvents() {
    Analysis analysis = analyser.analyse(eventRepository);
    analysedEvents = analysis.getAnalysedEvents();
    this.notifyDataSetChanged();
  }

  private View createEventItemView() {
    return createView(R.layout.event_list_item_view);
  }

  private void bindEventToItemView(final int position, View convertView) {
    AnalysedEvent analysedEventToBind = getGroup(position);
    TextView eventNameTextView = (TextView) convertView.findViewById(R.id.eventNameTextView);
    eventNameTextView.setText(analysedEventToBind.getEventName());
    TextView evenCountTextView = (TextView) convertView.findViewById(R.id.eventCountTextView);
    evenCountTextView.setText(Integer.toString(analysedEventToBind.getCount()));
    Button addEventSampleBtn = (Button) convertView.findViewById(R.id.addEventSampleButton);
    // NOTE: we've got to make the button not focusable because otherwise the button steals clicks from the expand
    // button.
    addEventSampleBtn.setFocusable(false);
    addEventSampleBtn.setOnClickListener(new AddEventButtonClickListener(position));
    convertView.setPadding(60, 0, 0, 0);
  }

  private View createChildView() {
    return createView(R.layout.event_timestamp_list_view);
  }

  private void bindTimestampsToView(int groupPosition, View childView) {
    ListView timestampsListView = (ListView) childView.findViewById(R.id.eventTimestamps_listView);
    timestampsListView.setAdapter(new EventTimestampsListAdapter(groupPosition));
  }

  private void addEvent(int position) {
    AnalysedEvent analysedEvent = getGroup(position);
    addEvent(analysedEvent.getEventName());
  }

  private View createView(int viewId) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    return inflater.inflate(viewId, null);
  }
  private class AddEventButtonClickListener implements View.OnClickListener {

    private final int position;

    public AddEventButtonClickListener(int position) {this.position = position;}
    @Override
    public void onClick(View v) {
      addEvent(position);
    }

  }

  private class EventTimestampsListAdapter extends BaseAdapter {
    private final List<Long> eventTimestamps;

    private final String eventName;

    public EventTimestampsListAdapter(int groupPosition) {
      eventName = getGroup(groupPosition).getEventName();
      eventTimestamps = eventRepository.timestampsOf(eventName);
    }

    @Override
    public int getCount() {
      return eventTimestamps.size();
    }

    @Override
    public Long getItem(int position) {
      return eventTimestamps.get(position);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View eventTimestampItemView = convertView == null ? createView(R.layout.event_timesamp_list_item) : convertView;
      bindTimestampToView(position, eventTimestampItemView);
      return eventTimestampItemView;
    }
    private void bindTimestampToView(final int position, View eventTimestampItemView) {
      TextView eventTimestampTextView = (TextView) eventTimestampItemView.findViewById(R.id.eventTimestamp_timestampTextView);
      eventTimestampTextView.setText(new Date(getItem(position)).toString());
      Button eventTimestampRemoveButton = (Button) eventTimestampItemView.findViewById(R.id.eventTimestamp_removeButton);
      eventTimestampRemoveButton.setOnClickListener(new RemoveTimestampButtonClickListener(position));
    }

    private class RemoveTimestampButtonClickListener implements View.OnClickListener {
      private final int position;

      public RemoveTimestampButtonClickListener(int position) {this.position = position;}

      @Override
      public void onClick(View v) {
        removeTimestamp(eventName, getItem(position));
      }
    }
  }
}
