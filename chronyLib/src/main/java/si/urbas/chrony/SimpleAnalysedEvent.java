package si.urbas.chrony;

public class SimpleAnalysedEvent implements AnalysedEvent {

  private final String name;
  private final int count;
  private final float relevance;

  protected SimpleAnalysedEvent(String name, int count, float relevance) {
    this.name = name;
    this.count = count;
    this.relevance = relevance;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getCount() {
    return count;
  }

  @Override
  public float getRelevance() {
    return relevance;
  }
}
