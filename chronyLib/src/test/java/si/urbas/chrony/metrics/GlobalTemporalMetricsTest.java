package si.urbas.chrony.metrics;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GlobalTemporalMetricsTest {

  private ArrayList<EventTemporalMetrics> eventMetricsList;

  @Before
  public void setUp() throws Exception {
    eventMetricsList = new ArrayList<EventTemporalMetrics>();
    eventMetricsList.add(new EventTemporalMetrics("event A", 3, 100, 50));
    eventMetricsList.add(new EventTemporalMetrics("event B", 3, 150, 75));
    eventMetricsList.add(new EventTemporalMetrics("event C", 3, 80, 25));
  }

  @Test(expected = IllegalArgumentException.class)
  public void calculateMetrics_MUST_throw_an_exception_WHEN_no_event_time_metrics_are_given() {
    GlobalTemporalMetrics.calculate(new ArrayList<EventTemporalMetrics>());
  }

  @Test
  public void calculateMetrics_MUST_extract_the_oldest_timestamp() {
    GlobalTemporalMetrics globalTemporalMetrics = GlobalTemporalMetrics.calculate(eventMetricsList);
    assertEquals(25, globalTemporalMetrics.oldestTimestamp);
  }
}