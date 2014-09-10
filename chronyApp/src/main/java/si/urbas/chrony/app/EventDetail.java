package si.urbas.chrony.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import si.urbas.chrony.Event;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.analysis.FrequencyAnalysis;
import si.urbas.chrony.app.data.SQLiteEventRepository;

import java.util.Date;


public class EventDetail extends Activity {

  public static final String INTENT_PARAMETER_EVENT_NAME = "eventDetail.eventName";
  private EventRepository eventRepository;
  private TextView eventNameTextView;
  private TextView frequencyTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupEventRepository();
    Event eventToShow = getEventToShow();
    FrequencyAnalysis eventFrequencyAnalysis = new FrequencyAnalysis(eventRepository, eventToShow.getEventName());
    bindViewsToFields();
    showEventDetails(eventToShow, eventFrequencyAnalysis);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.event_detail, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    return id == R.id.action_settings || super.onOptionsItemSelected(item);
  }

  public static void startEventDetailActivity(Context context, Event eventToDisplay) {
    Intent intent = new Intent(context, EventDetail.class);
    intent.putExtra(INTENT_PARAMETER_EVENT_NAME, eventToDisplay.getEventName());
    context.startActivity(intent);
  }


  private void setupEventRepository() {
    eventRepository = new SQLiteEventRepository(this);
  }

  private void bindViewsToFields() {
    setContentView(R.layout.activity_event_detail);
    eventNameTextView = (TextView) findViewById(R.id.eventDetail_eventNameTextView);
    frequencyTextView = (TextView) findViewById(R.id.eventDetail_frequencyTextView);
  }

  private Event getEventToShow() {
    String nameOfEventToShow = getIntent().getStringExtra(INTENT_PARAMETER_EVENT_NAME);
    return eventRepository.getEvent(nameOfEventToShow);
  }

  private void showEventDetails(Event eventToShow, FrequencyAnalysis eventFrequencyAnalysis) {
    eventNameTextView.setText(eventToShow.getEventName());
    frequencyTextView.setText((eventFrequencyAnalysis.allTimeFrequency(new Date().getTime()) * 7) + " per week");
  }
}
