package si.urbas.chrony;

public class FrequencyOnlyAnalyzer implements Analyzer {
  private final EventGrouper eventGrouper;

  public FrequencyOnlyAnalyzer(EventGrouper eventGrouper) {
    this.eventGrouper = eventGrouper;
  }

  @Override
  public Analysis analyze(EventRepository eventRepository) {
    Iterable<Event> events = eventGrouper.extractEventGroups(eventRepository);
    return new SimpleAnalysis(events);
  }
}
