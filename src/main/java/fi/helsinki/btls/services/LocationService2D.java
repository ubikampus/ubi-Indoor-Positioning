
package fi.helsinki.btls.services;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import java.util.*;
import java.util.stream.Collectors;
import fi.helsinki.btls.domain.Beacon;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;


/**
 * Location service class.
 */
public class LocationService2D implements ILocationService {
    private IObserverService iObserverService;

    public LocationService2D(IObserverService iObserverService) {
        this.iObserverService = iObserverService;
    }

    @Override
    public LocationModel calculateLocation(Beacon beacon) {
        List<ObservationModel> obs = beacon.getObservations();

        if (!obs.isEmpty()) {
            LeastSquaresOptimizer.Optimum optimum = ILocationService.createOptimum(beacon.getMinVolume(), new ArrayList<>(), obs, iObserverService);

            // gotten location in form of [x,y]
            double[] centroid = optimum.getPoint().toArray();

            // should be in form of [x,y]
            double[] standardDeviation = optimum.getSigma(0).toArray();

            //covariance matrix
            RealMatrix covMatrix = optimum.getCovariances(0);

            // alignment of principal eigenvector compared to axis.
            EigenDecomposition ed = new EigenDecomposition(covMatrix);
            double[] realEigenvalues = ed.getRealEigenvalues();
            RealVector principal = ed.getEigenvector(0);
            double highestEigenValue = realEigenvalues[0];

            for (int i = 1; i < realEigenvalues.length; i++) {
                RealVector eigenvector = ed.getEigenvector(i);
                double eigenvalue = realEigenvalues[i];

                if (highestEigenValue < eigenvalue) {
                    principal = eigenvector;
                    highestEigenValue = eigenvalue;
                }
            }

            double[] asArray = principal.toArray();
            double arctan = Math.atan(asArray[0] / asArray[1]); // uncertain which way should be the division...

            return new LocationModel(beacon.getId(),
                    centroid[0], centroid[1], 0,
                    standardDeviation[0], standardDeviation[1], 0, arctan);
        }

        return null;
    }

    @Override
    public List<LocationModel> calculateAllLocations(List<Beacon> beacons) {
        return beacons.stream().map(this::calculateLocation).collect(Collectors.toList());
    }
}
