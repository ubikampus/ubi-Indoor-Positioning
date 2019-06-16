
package fi.helsinki.btls.services;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.linear.RealMatrix;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import fi.helsinki.btls.domain.Beacon;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;

/**
 * Calculates location in 3 dimensional space.
 */
public class LocationService3D implements ILocationService {
    private IObserverService iObserverService;

    public LocationService3D(IObserverService iObserverService) {
        this.iObserverService = iObserverService;
    }

    @Override
    public LocationModel calculateLocation(Beacon beacon) {
        List<ObservationModel> obs = beacon.getObservations();

        if (!obs.isEmpty()) {
            LeastSquaresOptimizer.Optimum optimum = ILocationService.createOptimum(beacon.getMinRSSI(), new ArrayList<>(), obs, iObserverService);

            // gotten location in form of [x,y,z]
            double[] centroid = optimum.getPoint().toArray();

            // should be in form of [x,y,z]
            double[] standardDeviation = optimum.getSigma(0).toArray();

            //covariance matrix
            RealMatrix covMatrix = optimum.getCovariances(0);

            return new LocationModel(beacon.getId(),
                    centroid[0], centroid[1], centroid[2],
                    standardDeviation[0], standardDeviation[1], standardDeviation[2], 0);
        }

        return null;
    }

    @Override
    public List<LocationModel> calculateAllLocations(List<Beacon> beacons) {
        return beacons.stream().map(this::calculateLocation).collect(Collectors.toList());
    }
}
