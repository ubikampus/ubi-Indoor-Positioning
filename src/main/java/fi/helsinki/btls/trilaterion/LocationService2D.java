
package fi.helsinki.btls.trilaterion;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import java.util.ArrayList;
import java.util.List;
import fi.helsinki.btls.datamodels.*;
import fi.helsinki.btls.utils.IObserverService;


/**
 * Calculates location in two dimensional space.
 */
public class LocationService2D extends LocationService {
    public LocationService2D(IObserverService iObserverService) {
        this.iObserverService = iObserverService;
    }

    @Override
    public Location calculateLocation(Beacon beacon) {
        List<Observation> obs = beacon.getObservations();

        if (!obs.isEmpty()) {
            LeastSquaresOptimizer.Optimum optimum = createOptimum(beacon.getMinRSSI(), new ArrayList<>(), obs);

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

            return new Location2D(beacon.getId(),
                    centroid[0], centroid[1],
                    standardDeviation[0], standardDeviation[1], arctan);
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
