package si.urbas.chrony.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import si.urbas.chrony.Event;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.InMemoryEventRepository;

import java.util.Date;


public class DataEntry extends Activity {

  private final EventRepository eventRepository = new InMemoryEventRepository();
  private Button addEventButton;
  private ListView eventsListView;
  private EditText eventNameTextField;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_data_entry);

    addEventButton = (Button) findViewById(R.id.addEventButton);
    eventsListView = (ListView) findViewById(R.id.eventsListView);
    eventNameTextField = (EditText)findViewById(R.id.eventNameTextField);

    refreshEventListView();

    addEventButton.setOnClickListener(new EventAddClickListener());
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
    return id == R.id.action_settings || super.onOptionsItemSelected(item);
  }

  private void addNewEventFromEditField() {
    String eventName = eventNameTextField.getText().toString();
    long eventTimestamp = new Date().getTime();
    Event newEvent = new Event(eventName, eventTimestamp);
    eventRepository.addEvent(newEvent);
    refreshEventListView();
  }

  private void refreshEventListView() {
    eventsListView.setAdapter(EventListViewHelpers.toListAdapter(this, eventRepository));
  }

  private class EventAddClickListener implements View.OnClickListener {
    @Override
    public void onClick(View view) {
      addNewEventFromEditField();
    }
  }
}
