package si.urbas.chrony.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.analysis.SimpleAnalyser;
import si.urbas.chrony.app.data.SqliteEventRepository;
import si.urbas.chrony.app.io.EventRepositoryBackup;


public class DataEntry extends Activity {

  private static final String EVENT_REPOSITORY_BACKUP_FILE = "event_repository_backup";
  private EventRepository eventRepository;
  private ListView eventsListView;
  private EditText eventNameTextField;
  private Button addEventButton;
  private EventAnalysisListAdapter eventAnalysisListAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupEventRepository();
    bindViewToFields();
    setupEventListView();
    registerUiEventHandlers();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.data_entry, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    switch (id) {
      case R.id.action_save_database:
        saveRepositoryToFile();
        return false;
      case R.id.action_load_database:
        loadRepositoryFromFile();
        return false;
      case R.id.action_clear_database:
        eventAnalysisListAdapter.clear();
        return false;
      default:
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
  }

  private void setupEventRepository() {
    eventRepository = new SqliteEventRepository(this);
  }

  private void bindViewToFields() {
    setContentView(R.layout.activity_data_entry);
    addEventButton = (Button) findViewById(R.id.addEventButton);
    eventsListView = (ListView) findViewById(R.id.eventsListView);
    eventNameTextField = (EditText) findViewById(R.id.eventNameTextField);
  }

  private void saveRepositoryToFile() {
    EventRepositoryBackup.storeToFile(EVENT_REPOSITORY_BACKUP_FILE, eventRepository);
  }

  private void loadRepositoryFromFile() {
    EventRepositoryBackup.restoreFromFile(EVENT_REPOSITORY_BACKUP_FILE, eventRepository);
    setupEventListView();
  }

  private void setupEventListView() {
    eventAnalysisListAdapter = new EventAnalysisListAdapter(this, new SimpleAnalyser(), eventRepository);
    eventsListView.setAdapter(eventAnalysisListAdapter);
  }

  private void registerUiEventHandlers() {
    addEventButton.setOnClickListener(new EventAddClickListener());
    eventNameTextField.setOnEditorActionListener(new EventNameEditorActionListener());
    eventsListView.setOnItemClickListener(new EventItemClickListener());
  }

  private void addNewEventFromEditField() {
    String eventName = eventNameTextField.getText().toString();
    addNewEvent(eventName);
  }

  private void addNewEvent(String eventName) {
    eventAnalysisListAdapter.addEvent(eventName);
  }

  private class EventAddClickListener implements View.OnClickListener {
    @Override
    public void onClick(View view) {
      addNewEventFromEditField();
    }
  }

  private class EventNameEditorActionListener implements TextView.OnEditorActionListener {
    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
      addNewEventFromEditField();
      return false;
    }
  }

  private class EventItemClickListener implements AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
      EventAnalysisListAdapter eventListAdapter = (EventAnalysisListAdapter) adapterView.getAdapter();
      addNewEvent(eventListAdapter.getEventName(i));
    }
  }
}
