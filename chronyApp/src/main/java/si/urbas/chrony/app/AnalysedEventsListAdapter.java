package si.urbas.chrony.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import si.urbas.chrony.*;
import si.urbas.chrony.analysis.SimpleAnalyser;
import si.urbas.chrony.util.ChangeListener;

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
    this.eventRepository.registerChangeListener(new EventRepositoryChangeListener());
    refreshAnalysedEvents();
  }

  @Override
  public int getGroupCount() {
    return analysedEvents.size();
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return eventRepository.timestampsOf(getGroup(groupPosition).getEventName()).size();
  }

  @Override
  public AnalysedEvent getGroup(int groupPosition) {
    return analysedEvents.get(groupPosition);
  }

  @Override
  public Long getChild(int groupPosition, int childPosition) {
    String eventName = getGroup(groupPosition).getEventName();
    return eventRepository.timestampsOf(eventName).get(childPosition);
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
    bindTimestampToView(groupPosition, childPosition, childView);
    return childView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

  private void refreshAnalysedEvents() {
    Analysis analysis = analyser.analyse(eventRepository);
    analysedEvents = analysis.getAnalysedEvents();
    this.notifyDataSetChanged();
  }

  private View createEventItemView() {
    return inflateViewFromId(R.layout.event_list_item_view);
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
    return inflateViewFromId(R.layout.event_timesamp_list_item);
  }

  private void bindTimestampToView(int groupPosition, int childPosition, View eventTimestampItemView) {
    TextView eventTimestampTextView = (TextView) eventTimestampItemView.findViewById(R.id.eventTimestamp_timestampTextView);
    eventTimestampTextView.setText(new Date(getChild(groupPosition, childPosition)).toString());
    Button eventTimestampRemoveButton = (Button) eventTimestampItemView.findViewById(R.id.eventTimestamp_removeButton);
    eventTimestampRemoveButton.setOnClickListener(new RemoveTimestampButtonClickListener(groupPosition, childPosition));
  }

  private void addEvent(int position) {
    AnalysedEvent analysedEvent = getGroup(position);
    eventRepository.addEvent(new Event(analysedEvent.getEventName()));
  }

  private void removeTimestamp(int groupPosition, int childPosition) {
    String eventName = getGroup(groupPosition).getEventName();
    Long timestamp = getChild(groupPosition, childPosition);
    eventRepository.removeTimestamp(eventName, timestamp);
  }

  private View inflateViewFromId(int layoutId) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    return inflater.inflate(layoutId, null);
  }

  private class AddEventButtonClickListener implements View.OnClickListener {

    private final int position;

    public AddEventButtonClickListener(int position) {this.position = position;}

    @Override
    public void onClick(View v) {
      addEvent(position);
    }

  }

  private class RemoveTimestampButtonClickListener implements View.OnClickListener {

    private final int groupPosition;
    private final int childPosition;

    public RemoveTimestampButtonClickListener(int groupPosition, int childPosition) {
      this.groupPosition = groupPosition;
      this.childPosition = childPosition;
    }

    @Override
    public void onClick(View v) {
      removeTimestamp(groupPosition, childPosition);
    }

  }

  private class EventRepositoryChangeListener implements ChangeListener {
    @Override
    public void changed() {
      refreshAnalysedEvents();
    }
  }
}
