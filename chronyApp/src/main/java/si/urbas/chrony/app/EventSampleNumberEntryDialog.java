package si.urbas.chrony.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import si.urbas.chrony.AnalysedEvent;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.EventSample;

import static si.urbas.chrony.app.util.InflaterUtils.inflateLayout;

public class EventSampleNumberEntryDialog {

  public static void show(Context context, EventRepository eventRepository, AnalysedEvent analysedEvent) {
    View dialogView = inflateLayout(context, R.layout.event_sample_number_entry);
    AddEventSampleNumberClickListener addEventSampleNumberClickListener = new AddEventSampleNumberClickListener(eventRepository, analysedEvent, context, dialogView);
    Dialog numberEntryDialog = createDialog(context, dialogView, addEventSampleNumberClickListener);
    setupView(dialogView, analysedEvent);
    numberEntryDialog.show();
  }

  private static Dialog createDialog(Context context, View dialogView, AddEventSampleNumberClickListener addEventSampleNumberClickListener) {
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
    return dialogBuilder.setView(dialogView)
                        .setNegativeButton("Cancel", new AbortClickListener())
                        .setPositiveButton("Okay", addEventSampleNumberClickListener)
                        .create();
  }

  private static void setupView(View numberEntryDialog, AnalysedEvent analysedEvent) {
    TextView titleTextView = (TextView) numberEntryDialog.findViewById(R.id.eventSampleNumberEntry_title);
    String title = "Enter the value of '" + analysedEvent.getUnderlyingEvent().getEventName() + "':";
    titleTextView.setText(title);
  }

  private static class AddEventSampleNumberClickListener implements DialogInterface.OnClickListener {

    private final EventRepository eventRepository;
    private final AnalysedEvent analysedEvent;
    private final Context context;
    private final View dialogView;

    public AddEventSampleNumberClickListener(EventRepository eventRepository, AnalysedEvent analysedEvent, Context context, View dialogView) {
      this.eventRepository = eventRepository;
      this.analysedEvent = analysedEvent;
      this.context = context;
      this.dialogView = dialogView;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
      String numberAsText = getNumberEditText().getText().toString();
      try {
        double number = Double.parseDouble(numberAsText);
        eventRepository.addEventSample(new EventSample(analysedEvent.getUnderlyingEvent().getEventName(), number));
      } catch (NumberFormatException e) {
        new AlertDialog.Builder(context).setTitle("Invalid number format.").create().show();
      }
    }

    private EditText getNumberEditText() {
      return (EditText) dialogView.findViewById(R.id.eventSampleNumberEntry_numberEditText);
    }

  }

  private static class AbortClickListener implements DialogInterface.OnClickListener {
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
  }

}
