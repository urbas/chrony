package si.urbas.chrony;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class AnalysisInterpreterTest {

  private AnalysisInterpreter analysisInterpreter;
  private Analysis analysis;

  @Before
  public void setUp() throws Exception {
    analysisInterpreter = new AnalysisInterpreter();
    analysis = mock(Analysis.class);
  }

  @Test
  public void mostRelevantEvents_MUST_return_an_empty_iterable() {
      assertThat(
        analysisInterpreter.mostRelevantEvents(analysis),
        emptyIterable()
      );
  }
}