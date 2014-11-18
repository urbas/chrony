package si.urbas.chrony.recurrence.analysis;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import si.urbas.chrony.EventSample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static si.urbas.chrony.util.TimeUtils.MINUTE_IN_MILLIS;

public class TimeOfDayClusterer {
  
  public int[] millisOfDayClusters(List<EventSample> eventSamples) {
    DBSCANClusterer<DoublePoint> doublePointDBSCANClusterer = new DBSCANClusterer<DoublePoint>(10 * MINUTE_IN_MILLIS, 2);
    List<Cluster<DoublePoint>> clusters = doublePointDBSCANClusterer.cluster(convertToDoublePoints(eventSamples));
    return toAverageMillisOfDay(clusters);
  }

  private int[] toAverageMillisOfDay(List<Cluster<DoublePoint>> clusters) {
    int[] millisOfDay = new int[clusters.size()];
    for (int i = 0, clustersSize = clusters.size(); i < clustersSize; i++) {
      millisOfDay[i] = calculateAverage(clusters.get(i));
    }
    return millisOfDay;
  }

  private int calculateAverage(Cluster<DoublePoint> cluster) {
    long sumOfMillis = 0;
    for (DoublePoint point : cluster.getPoints()) {
      sumOfMillis += point.getPoint()[0];
    }
    return (int) (sumOfMillis / cluster.getPoints().size());
  }

  private static Collection<DoublePoint> convertToDoublePoints(List<EventSample> eventSamples) {
    ArrayList<DoublePoint> doublePoints = new ArrayList<DoublePoint>();
    for (EventSample eventSample : eventSamples) {
      doublePoints.add(new DoublePoint(new double[]{eventSample.getTimestamp().getMillisOfDay()}));
    }
    return doublePoints;
  }
}
