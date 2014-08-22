package si.urbas.chrony;

public class SimpleAnalysedEvent implements AnalysedEvent {

  private final String name;
  private final int count;

  protected SimpleAnalysedEvent(String name, int count) {
    this.name = name;
    this.count = count;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getCount() {
    return count;
  }
}
