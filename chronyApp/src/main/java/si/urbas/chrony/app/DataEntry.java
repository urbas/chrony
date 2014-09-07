package si.urbas.chrony.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import si.urbas.chrony.Event;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.analysis.SimpleAnalyser;
import si.urbas.chrony.app.data.SQLiteEventRepository;
import si.urbas.chrony.app.io.EventRepositoryBackup;


public class DataEntry extends Activity {

  private static final String EVENT_REPOSITORY_BACKUP_FILE = "event_repository_backup_v2.corrected";
  private EventRepository eventRepository;
  private EditText eventNameTextField;
  private Button addEventButton;
  private ExpandableListView analysedEventsListView;
  private Spinner dataTypeChooser;
  private AnalysedEventsListAdapter analysedEventsListAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupEventRepository();
    bindViewToFields();
    bindEventsToListView();
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
        eventRepository.clear();
        return false;
      default:
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
  }

  private void setupEventRepository() {
    eventRepository = new SQLiteEventRepository(this);
  }

  private void bindViewToFields() {
    setContentView(R.layout.activity_data_entry);
    addEventButton = (Button) findViewById(R.id.addEventButton);
    eventNameTextField = (EditText) findViewById(R.id.eventNameTextField);
    analysedEventsListView = (ExpandableListView) findViewById(R.id.analysedEventsListView);
    setupDataTypeChooser();
  }

  private void setupDataTypeChooser() {
    dataTypeChooser = (Spinner) findViewById(R.id.dataEntry_dataTYpeSpinner);
    ArrayAdapter<CharSequence> dataTypeChooserAdapter = ArrayAdapter.createFromResource(this, R.array.event_DataTypes, android.R.layout.simple_spinner_item);
    dataTypeChooserAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    dataTypeChooser.setAdapter(dataTypeChooserAdapter);
  }

  private void saveRepositoryToFile() {
    EventRepositoryBackup.storeToFile(EVENT_REPOSITORY_BACKUP_FILE, eventRepository);
  }

  private void loadRepositoryFromFile() {
    EventRepositoryBackup.restoreFromFile(EVENT_REPOSITORY_BACKUP_FILE, eventRepository);
  }

  private void bindEventsToListView() {
    analysedEventsListAdapter = new AnalysedEventsListAdapter(this, new SimpleAnalyser(), eventRepository);
    analysedEventsListView.setAdapter(analysedEventsListAdapter);
  }

  private void registerUiEventHandlers() {
    addEventButton.setOnClickListener(new EventAddClickListener());
    eventNameTextField.setOnEditorActionListener(new EventNameEditorActionListener());
  }

  private void addNewEventFromEditField() {
    String eventName = eventNameTextField.getText().toString();
    int dataType = convertSelectedItemToDataTypeId(dataTypeChooser.getSelectedItemPosition());
    addNewEvent(eventName, dataType);
  }

  private int convertSelectedItemToDataTypeId(int selectedItemPosition) {
    return selectedItemPosition;
  }

  private void addNewEvent(String eventName, int dataType) {
    eventRepository.addEvent(new Event(eventName, dataType));
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

}
