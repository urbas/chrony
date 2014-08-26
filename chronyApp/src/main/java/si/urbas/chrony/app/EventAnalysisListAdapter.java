package si.urbas.chrony.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import si.urbas.chrony.AnalysedEvent;
import si.urbas.chrony.Analysis;

import java.util.*;

public class EventAnalysisListAdapter extends BaseAdapter {

  private final Context context;
  private final List<AnalysedEvent> analysedEvents;

  public EventAnalysisListAdapter(Context context, Analysis analysis) {
    this.context = context;
    this.analysedEvents = analysis.getAnalysedEvents();
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

  private void bindEventToItemView(int position, View convertView) {
    AnalysedEvent analysedEventToBind = analysedEvents.get(position);
    TextView eventNameTextView = (TextView) convertView.findViewById(R.id.eventNameTextView);
    eventNameTextView.setText(analysedEventToBind.getEventName());
    TextView evenCountTextView = (TextView) convertView.findViewById(R.id.eventCountTextView);
    evenCountTextView.setText(Integer.toString(analysedEventToBind.getCount()));
  }

}
