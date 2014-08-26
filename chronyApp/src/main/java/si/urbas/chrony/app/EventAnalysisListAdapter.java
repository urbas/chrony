package si.urbas.chrony.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import si.urbas.chrony.AnalysedEvent;
import si.urbas.chrony.Analysis;
import si.urbas.chrony.Event;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.analysis.SimpleAnalyser;

import java.util.*;

public class EventAnalysisListAdapter extends BaseAdapter {

  private final Context context;
  private final SimpleAnalyser analysisFactory;
  private final EventRepository eventRepository;
  private List<AnalysedEvent> analysedEvents;

  public EventAnalysisListAdapter(Context context, SimpleAnalyser analysisFactory, EventRepository eventRepository) {
    this.context = context;
    this.analysisFactory = analysisFactory;
    this.eventRepository = eventRepository;
    refreshAnalysedEvents();
  }

  private void refreshAnalysedEvents() {
    Analysis analysis = analysisFactory.analyse(eventRepository);
    analysedEvents = analysis.getAnalysedEvents();
    this.notifyDataSetChanged();
  }

  public void addEvent(String eventName) {
    eventRepository.addEvent(new Event(eventName, new Date().getTime()));
    refreshAnalysedEvents();
  }

  public void clear() {
    eventRepository.clear();
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return analysedEvents.size();
  }

  @Override
  public AnalysedEvent getItem(int position) {
    return analysedEvents.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = createEventItemView();
    }
    bindEventToItemView(position, convertView);
    return convertView;
  }

  public String getEventName(int i) {
    return analysedEvents.get(i).getEventName();
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
  }

  private void addEvent(int position) {
    AnalysedEvent analysedEvent = getItem(position);
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
