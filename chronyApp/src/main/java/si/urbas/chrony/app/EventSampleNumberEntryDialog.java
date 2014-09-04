package si.urbas.chrony.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import si.urbas.chrony.AnalysedEvent;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.EventSample;

import static si.urbas.chrony.app.util.InflaterUtils.inflateLayout;

public class EventSampleNumberEntryDialog {

  private final Context context;
  private final EventRepository eventRepository;

  public EventSampleNumberEntryDialog(Context context, EventRepository eventRepository) {
    this.context = context;
    this.eventRepository = eventRepository;
  }

  void showFor(AnalysedEvent analysedEvent) {
    String title = "Enter the number for '" + analysedEvent.getUnderlyingEvent().getEventName() + "'";
    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
    AlertDialog numberEntryDialog = dialogBuilder.setView(inflateLayout(context, R.layout.event_sample_number_entry))
                                                 .setNegativeButton("Cancel", new AbortClickListener())
                                                 .setPositiveButton("Okay", new AddEventSampleNumberClickListener(analysedEvent))
                                                 .create();
    numberEntryDialog.show();
  }

  private class AddEventSampleNumberClickListener implements DialogInterface.OnClickListener {

    private final AnalysedEvent analysedEvent;

    public AddEventSampleNumberClickListener(AnalysedEvent analysedEvent) {
      this.analysedEvent = analysedEvent;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
      eventRepository.addEventSample(new EventSample(analysedEvent.getUnderlyingEvent().getEventName()));
    }

  }

  private static class AbortClickListener implements DialogInterface.OnClickListener {
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
  }

}
