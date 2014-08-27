package si.urbas.chrony.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
    String eventName = getGroup(0).getEventName();
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
    return convertView == null ? getTextView() : convertView;
  }

  public void clear() {
    eventRepository.clear();
    refreshAnalysedEvents();
  }

  private RelativeLayout getTextView() {
    TextView textView = new TextView(context);
    textView.setText("Hello world!");
    RelativeLayout relativeLayout = new RelativeLayout(context);
    relativeLayout.addView(textView);
    return relativeLayout;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return false;
  }

  public void addEvent(String eventName) {
    eventRepository.addEvent(new Event(eventName, new Date().getTime()));
    refreshAnalysedEvents();
  }

  private void refreshAnalysedEvents() {
    Analysis analysis = analyser.analyse(eventRepository);
    analysedEvents = analysis.getAnalysedEvents();
    this.notifyDataSetChanged();
  }

  private View createEventItemView() {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    return inflater.inflate(R.layout.event_list_item_view, null);
  }

  private void bindEventToItemView(final int position, View convertView) {
    AnalysedEvent analysedEventToBind = analysedEvents.get(position);
    TextView eventNameTextView = (TextView) convertView.findViewById(R.id.eventNameTextView);
    eventNameTextView.setText(analysedEventToBind.getEventName());
    TextView evenCountTextView = (TextView) convertView.findViewById(R.id.eventCountTextView);
    evenCountTextView.setText(Integer.toString(analysedEventToBind.getCount()));
    Button addEventSampleBtn = (Button) convertView.findViewById(R.id.addEventSampleTextView);
    addEventSampleBtn.setOnClickListener(new AddEventButtonClickListener(position));
    convertView.setPadding(36, 0, 0, 0);
  }

  private void addEvent(int position) {
    AnalysedEvent analysedEvent = getGroup(position);
    addEvent(analysedEvent.getEventName());
  }

  private class AddEventButtonClickListener implements View.OnClickListener {
    private final int position;

    public AddEventButtonClickListener(int position) {this.position = position;}

    @Override
    public void onClick(View v) {
      addEvent(position);
    }
  }

}
