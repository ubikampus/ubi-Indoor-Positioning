
package fi.helsinki.btls.services;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.linear.RealMatrix;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import fi.helsinki.btls.domain.Beacon;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;

/**
 * Location service class.
 */
public class LocationService3D implements ILocationService {
    private Map<String, double[]> rasps;

    public LocationService3D() {
        rasps = new HashMap<>();
    }

    public double[] addRasp(String raspId, double x, double y, double z) {
        return rasps.put(raspId, new double[]{x, y, z});
    }

    public boolean addAllRasps(Map<String, double[]> newOnes) {
        for (Map.Entry<String, double[]> e : newOnes.entrySet()) {
            if (e.getValue().length != 3) {
                return false;
            }
        }

        rasps.putAll(newOnes);
        return true;
    }

    public double[] deleteRasp(String raspId) {
        return rasps.remove(raspId);
    }

    @Override
    public void deleteAllRasps() {
        rasps.clear();
    }

    @Override
    public LocationModel calculateLocation(Beacon beacon) {
        List<String> raspsChecked = new ArrayList<>();
        List<ObservationModel> obs = beacon.getObservations();

        if (!obs.isEmpty()) {
            LeastSquaresOptimizer.Optimum optimum = ILocationService.createOptimum(beacon, raspsChecked, obs, rasps);

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
