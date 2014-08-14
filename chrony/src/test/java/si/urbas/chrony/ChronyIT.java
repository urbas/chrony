package si.urbas.chrony;

import org.junit.Test;

import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;

public class ChronyIT {

  @Test
  public void relevantEvents_MUST_return_an_empty_list() {
    Report report = new Report(new MemoryEventRepository(), new FrequencyOnlyAnalyzer(), new AnalysisInterpreter());
    assertThat(report.relevantEvents(), emptyIterable());
  }
}
