package si.urbas.chrony;

public class SimpleAnalysis implements Analysis {
  private final Iterable<Event> events;

  public SimpleAnalysis(Iterable<Event> events) {this.events = events;}

  @Override
  public Iterable<Event> allEvents() {
    return events;
  }
}
