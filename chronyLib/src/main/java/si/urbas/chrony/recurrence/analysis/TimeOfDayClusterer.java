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
    DBSCANClusterer<DoublePoint> doublePointDBSCANClusterer = new DBSCANClusterer<DoublePoint>(30 * MINUTE_IN_MILLIS, 2);
    List<Cluster<DoublePoint>> clusters = doublePointDBSCANClusterer.cluster(convertToDoublePoints(eventSamples));
    return toMillisOfDay(clusters);
  }

  private int[] toMillisOfDay(List<Cluster<DoublePoint>> clusters) {
    int[] millisOfDay = new int[clusters.size()];
    for (int i = 0, clustersSize = clusters.size(); i < clustersSize; i++) {
      Cluster<DoublePoint> cluster = clusters.get(i);
      millisOfDay[i] = (int) Math.round(cluster.getPoints().get(0).getPoint()[0]);
    }
    return millisOfDay;
  }

  private static Collection<DoublePoint> convertToDoublePoints(List<EventSample> eventSamples) {
    ArrayList<DoublePoint> doublePoints = new ArrayList<DoublePoint>();
    for (EventSample eventSample : eventSamples) {
      doublePoints.add(new DoublePoint(new double[]{eventSample.getTimestamp().getMillisOfDay()}));
    }
    return doublePoints;
  }
}
