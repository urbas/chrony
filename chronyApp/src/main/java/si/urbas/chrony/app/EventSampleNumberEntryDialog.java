package si.urbas.chrony.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.TextView;
import si.urbas.chrony.AnalysedEvent;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.EventSample;

import static si.urbas.chrony.app.util.InflaterUtils.inflateLayout;

public class EventSampleNumberEntryDialog {

  public static void show(Context context, EventRepository eventRepository, AnalysedEvent analysedEvent) {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
    AddEventSampleNumberClickListener addEventSampleNumberClickListener = new AddEventSampleNumberClickListener(eventRepository, analysedEvent, null);
    Dialog numberEntryDialog = dialogBuilder.setView(inflateLayout(context, R.layout.event_sample_number_entry))
                                            .setNegativeButton("Cancel", new AbortClickListener())
                                            .setPositiveButton("Okay", addEventSampleNumberClickListener)
                                            .create();
    setupView(numberEntryDialog, analysedEvent);
    addEventSampleNumberClickListener.setNumberEditText((EditText) numberEntryDialog.findViewById(R.id.eventSampleNumberEntry_numberEditText));
    numberEntryDialog.show();
  }

  private static void setupView(Dialog numberEntryDialog, AnalysedEvent analysedEvent) {
    TextView titleTextView = (TextView) numberEntryDialog.findViewById(R.id.eventSampleNumberEntry_title);
    String title = "Enter the number for '" + analysedEvent.getUnderlyingEvent().getEventName() + "':";
    titleTextView.setText(title);
  }

  private static class AddEventSampleNumberClickListener implements DialogInterface.OnClickListener {

    private final EventRepository eventRepository;
    private final AnalysedEvent analysedEvent;
    private final Context context;
    private EditText numberEditText;

    public AddEventSampleNumberClickListener(EventRepository eventRepository, AnalysedEvent analysedEvent, Context context) {
      this.eventRepository = eventRepository;
      this.analysedEvent = analysedEvent;
      this.context = context;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
      String numberAsText = numberEditText.getText().toString();
      try {
        double number = Double.parseDouble(numberAsText);
        eventRepository.addEventSample(new EventSample(analysedEvent.getUnderlyingEvent().getEventName(), number));
      } catch (NumberFormatException e) {
        new AlertDialog.Builder(context).setTitle("Invalid number format.").create().show();
      }
    }

    public void setNumberEditText(EditText viewById) {
      numberEditText = viewById;
    }
  }

  private static class AbortClickListener implements DialogInterface.OnClickListener {
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
  }

}
