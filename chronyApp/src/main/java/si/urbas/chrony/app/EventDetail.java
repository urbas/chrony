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
import si.urbas.chrony.EventSample;
import si.urbas.chrony.app.data.SQLiteEventRepository;
import si.urbas.chrony.descriptions.RecurrenceDescriptions;
import si.urbas.chrony.frequency.analysis.FrequencyAnalysis;
import si.urbas.chrony.recurrence.Recurrence;
import si.urbas.chrony.recurrence.analysis.RecurrenceAnalysers;

import java.util.Date;
import java.util.List;

import static si.urbas.chrony.util.TimeUtils.WEEK_IN_MILLIS;

public class EventDetail extends Activity {

  public static final String INTENT_PARAMETER_EVENT_NAME = "eventDetail.eventName";
  private EventRepository eventRepository;
  private TextView eventNameTextView;
  private TextView frequencyTextView;
  private TextView frequencyLastWeekTextView;
  private TextView recurrenceTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupEventRepository();
    bindViewsToFields();
    analyseAndVisualize();
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
    frequencyLastWeekTextView = (TextView) findViewById(R.id.eventDetail_frequencyLastWeekTextView);
    recurrenceTextView = (TextView) findViewById(R.id.eventDetail_recurrenceTextView);
  }

  private void analyseAndVisualize() {
    Event eventToShow = getEventToShow();
    List<EventSample> eventSamples = eventRepository.samplesOf(eventToShow.getEventName());
    FrequencyAnalysis frequencyAnalysis = new FrequencyAnalysis(eventSamples);
    List<Recurrence> recurrenceAnalysis = RecurrenceAnalysers.create(eventSamples).foundRecurrences();
    showEventDetails(eventToShow, frequencyAnalysis, recurrenceAnalysis);
  }

  private Event getEventToShow() {
    String nameOfEventToShow = getIntent().getStringExtra(INTENT_PARAMETER_EVENT_NAME);
    return eventRepository.getEvent(nameOfEventToShow);
  }

  private void showEventDetails(Event eventToShow,
                                FrequencyAnalysis frequencyAnalysis,
                                List<Recurrence> recurrenceAnalysis) {
    eventNameTextView.setText(eventToShow.getEventName());
    long now = new Date().getTime();
    frequencyTextView.setText(Integer.toString(frequencyAnalysis.occurrencesUntil(now)));
    frequencyLastWeekTextView.setText(Integer.toString(frequencyAnalysis.occurrencesWithin(now - WEEK_IN_MILLIS, now)));
    recurrenceTextView.setText(RecurrenceDescriptions.toShortDescription(recurrenceAnalysis));
  }
}
