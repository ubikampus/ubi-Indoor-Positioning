
package fi.helsinki.ubipositioning.trilateration;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.linear.RealMatrix;
import java.util.ArrayList;
import java.util.List;
import fi.helsinki.ubipositioning.datamodels.*;
import fi.helsinki.ubipositioning.utils.IObserverService;

/**
 * Calculates location in 3 dimensional space.
 */
public class LocationService3D extends LocationService {
    public LocationService3D(IObserverService iObserverService) {
        this.iObserverService = iObserverService;
    }

    @Override
    public Location calculateLocation(Beacon beacon) {
        List<Observation> obs = beacon.getObservations();

        if (!obs.isEmpty()) {
            LeastSquaresOptimizer.Optimum optimum = createOptimum(beacon.getMinRSSI(), new ArrayList<>(), obs);

            // gotten location in form of [x,y,z]
            double[] centroid = optimum.getPoint().toArray();

            // should be in form of [x,y,z]
            double[] standardDeviation = optimum.getSigma(0).toArray();

            //covariance matrix
            RealMatrix covMatrix = optimum.getCovariances(0);

            return new Location3D(beacon.getId(),
                    centroid[0], centroid[1], centroid[2],
                    standardDeviation[0], standardDeviation[1], standardDeviation[2]);
        }

        return null;
    }

    @Override
    public List<Location> calculateAllLocations(List<Beacon> beacons) {
        List<Location> list = new ArrayList<>();

        beacons.forEach(beacon -> {
            try {
                list.add(calculateLocation(beacon));
            } catch (Exception e) {
            }
        });

        return list;
    }
}
