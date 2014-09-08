package si.urbas.chrony.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import si.urbas.chrony.Event;


public class EventDetail extends Activity {

  public static final String INTENT_PARAMETER_EVENT_NAME = "eventDetail.eventName";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_event_detail);
    String eventName = getIntent().getStringExtra(INTENT_PARAMETER_EVENT_NAME);
    TextView eventNameTextView = (TextView) findViewById(R.id.eventDetail_eventNameTextView);
    eventNameTextView.setText(eventName);
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
}
