package si.urbas.chrony.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import si.urbas.chrony.*;
import si.urbas.chrony.analysis.AnalysedEvent;
import si.urbas.chrony.analysis.Analysis;
import si.urbas.chrony.analysis.SimpleAnalyser;
import si.urbas.chrony.util.ChangeListener;
import si.urbas.chrony.util.ChangeNotifier;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static si.urbas.chrony.app.util.InflaterUtils.inflateLayout;

public class AnalysedEventsListAdapter extends BaseExpandableListAdapter {

  private final Context context;
  private final SimpleAnalyser analyser;
  private final EventRepository eventRepository;
  private final ChangeNotifier eventRepositoryChangeNotifier;
  private List<AnalysedEvent> analysedEvents;
  private final DateFormat eventSampleTimestampFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
  private EventClickListener eventClickListener;

  public AnalysedEventsListAdapter(Context context, SimpleAnalyser analyser, EventRepository eventRepository, ChangeNotifier eventRepositoryChangeNotifier) {
    this.context = context;
    this.analyser = analyser;
    this.eventRepository = eventRepository;
    this.eventRepositoryChangeNotifier = eventRepositoryChangeNotifier;
    this.eventRepositoryChangeNotifier.registerChangeListener(new EventRepositoryChangeListener());
    refreshAnalysedEvents();
  }

  @Override
  public int getGroupCount() {
    return analysedEvents.size();
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return eventRepository.samplesOf(getNameOfEventAtPosition(groupPosition)).size();
  }

  @Override
  public AnalysedEvent getGroup(int groupPosition) {
    return analysedEvents.get(groupPosition);
  }

  @Override
  public EventSample getChild(int groupPosition, int childPosition) {
    String eventName = getNameOfEventAtPosition(groupPosition);
    return eventRepository.samplesOf(eventName).get(childPosition);
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
    bindEventSampleToView(groupPosition, childPosition, childView);
    return childView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

  public void setEventClickListener(EventClickListener eventClickListener) {
    this.eventClickListener = eventClickListener;
  }

  private void refreshAnalysedEvents() {
    Analysis analysis = analyser.analyse(eventRepository);
    analysedEvents = analysis.getAnalysedEvents();
    this.notifyDataSetChanged();
  }

  private View createEventItemView() {
    return inflateLayout(context, R.layout.event_list_item_view);
  }

  private void bindEventToItemView(final int position, View convertView) {
    AnalysedEvent analysedEventToBind = getGroup(position);

    TextView eventNameTextView = (TextView) convertView.findViewById(R.id.eventNameTextView);
    eventNameTextView.setText(analysedEventToBind.getUnderlyingEvent().getEventName());
    eventNameTextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        notifyEventClickListener(position);
      }
    });

    TextView evenCountTextView = (TextView) convertView.findViewById(R.id.eventCountTextView);
    evenCountTextView.setText(Integer.toString(analysedEventToBind.getCount()));

    Button addEventSampleBtn = (Button) convertView.findViewById(R.id.addEventSampleButton);
    // NOTE: we've got to make the button not focusable because otherwise the button steals clicks from the expand
    // button.
    addEventSampleBtn.setFocusable(false);
    addEventSampleBtn.setOnClickListener(new AddEventButtonClickListener(position));
    convertView.setPadding(60, 0, 0, 0);
  }

  private void notifyEventClickListener(int eventPosition) {
    Event clickedEvent = getGroup(eventPosition).getUnderlyingEvent();
    eventClickListener.eventClicked(clickedEvent);
  }

  private View createChildView() {
    return inflateLayout(context, R.layout.event_sample_list_item);
  }

  private void bindEventSampleToView(int groupPosition, int childPosition, View eventTimestampItemView) {
    EventSample eventSample = getChild(groupPosition, childPosition);
    TextView eventTimestampTextView = (TextView) eventTimestampItemView.findViewById(R.id.eventSampleListItem_timestampTextView);
    eventTimestampTextView.setText(getFormattedTimestamp(eventSample));
    TextView eventSampleDataTextView = (TextView) eventTimestampItemView.findViewById(R.id.eventSampleListItem_dataTextView);
    eventSampleDataTextView.setText(getFormattedData(eventSample));
    Button eventTimestampRemoveButton = (Button) eventTimestampItemView.findViewById(R.id.eventSampleListItem_removeButton);
    eventTimestampRemoveButton.setOnClickListener(new RemoveTimestampButtonClickListener(groupPosition, childPosition));
  }

  private String getFormattedData(EventSample eventSample) {
    return eventSample.getData() == null ? "" : eventSample.getData().toString();
  }

  private String getFormattedTimestamp(EventSample eventSample) {
    Date eventSampleDate = new Date(eventSample.getTimestamp().getMillis());
    return eventSampleTimestampFormat.format(eventSampleDate);
  }

  private void removeEventSample(int groupPosition, int childPosition) {
    String eventName = getNameOfEventAtPosition(groupPosition);
    long timestamp = getChild(groupPosition, childPosition).getTimestamp().getMillis();
    removeEventSample(eventName, timestamp);
  }

  private String getNameOfEventAtPosition(int positionInList) {
    return getGroup(positionInList).getUnderlyingEvent().getEventName();
  }

  private void addEventSample(AnalysedEvent analysedEvent) {
    if (isEventWithoutData(analysedEvent)) {
      addEventSampleWithoutData(analysedEvent.getUnderlyingEvent());
    } else {
      EventSampleNumberEntryDialog.show(context, eventRepository, eventRepositoryChangeNotifier, analysedEvent);
    }
  }


  private static boolean isEventWithoutData(AnalysedEvent analysedEvent) {
    return analysedEvent.getUnderlyingEvent().getDataType() == Event.NO_DATA_TYPE;
  }

  private void removeEventSample(String eventName, Long timestamp) {
    eventRepository.removeEventSample(eventName, timestamp);
    eventRepositoryChangeNotifier.notifyChangeListeners();
  }

  private void addEventSampleWithoutData(Event event) {
    eventRepository.addEventSample(new EventSample(event.getEventName()));
    eventRepositoryChangeNotifier.notifyChangeListeners();
  }

  private class AddEventButtonClickListener implements View.OnClickListener {

    private final int position;

    public AddEventButtonClickListener(int position) {this.position = position;}

    @Override
    public void onClick(View v) {
      addEventSample(getGroup(position));
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
      removeEventSample(groupPosition, childPosition);
    }

  }

  private class EventRepositoryChangeListener implements ChangeListener {
    @Override
    public void changed() {
      refreshAnalysedEvents();
    }
  }
}
