package si.urbas.chrony;

public class Chrony {

  private final EventRepository eventRepository;
  private final ReportFactory reportFactory;

  public Chrony(EventRepository eventRepository, ReportFactory reportFactory) {
    this.eventRepository = eventRepository;
    this.reportFactory = reportFactory;
  }

  public void addEvent(Event event) {
    eventRepository.addEvent(event);
  }

  public ChronyReport getReport() {
    return reportFactory.createReport(eventRepository);
  }

}
