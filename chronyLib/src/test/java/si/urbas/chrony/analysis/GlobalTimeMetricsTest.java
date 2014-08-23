package si.urbas.chrony.analysis;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GlobalTimeMetricsTest {

  private ArrayList<EventTimeMetrics> eventMetricsList;

  @Before
  public void setUp() throws Exception {
    eventMetricsList = new ArrayList<EventTimeMetrics>();
    eventMetricsList.add(new EventTimeMetrics("event A", 3, 100, 50));
    eventMetricsList.add(new EventTimeMetrics("event B", 3, 150, 75));
    eventMetricsList.add(new EventTimeMetrics("event C", 3, 80, 25));
  }

  @Test(expected = IllegalArgumentException.class)
  public void calculateMetrics_MUST_throw_an_exception_WHEN_no_event_time_metrics_are_given() {
    GlobalTimeMetrics.calculateGlobalMetrics(new ArrayList<EventTimeMetrics>());
  }

  @Test
  public void calculateMetrics_MUST_extract_the_oldest_timestamp() {
    GlobalTimeMetrics globalTimeMetrics = GlobalTimeMetrics.calculateGlobalMetrics(eventMetricsList);
    assertEquals(25, globalTimeMetrics.oldestTimestamp);
  }
}