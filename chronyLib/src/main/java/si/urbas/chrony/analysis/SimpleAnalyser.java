package si.urbas.chrony.analysis;

import si.urbas.chrony.EventRepository;

public class SimpleAnalyser {
  public Analysis analyse(EventRepository eventRepository) {
    return new SimpleAnalysis(eventRepository);
  }
}
