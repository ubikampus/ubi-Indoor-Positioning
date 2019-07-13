
package fi.helsinki.ubipositioning.trilateration;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import java.util.ArrayList;
import java.util.List;
import fi.helsinki.ubipositioning.datamodels.*;
import fi.helsinki.ubipositioning.utils.IObserverService;


/**
 * Trilateration in two dimensional space.
 */
public class LocationService2D extends LocationService {
    public LocationService2D(IObserverService iObserverService) {
        super();
        this.iObserverService = iObserverService;
    }

    /**
     * Calculates the location of given device in two dimensional space using trilateration.
     *
     * @param beacon Device which location wanted to be known.
     *
     * @see LocationService#createOptimum(double, List, List)
     *
     * @throws IllegalArgumentException if beacon hasn't been detected by BLE listeners yet.
     *
     * @return Location of given device.
     */
    @Override
    public Location calculateLocation(Beacon beacon) {
        List<Observation> obs = beacon.getObservations();

        if (obs.isEmpty()) {
            throw new IllegalArgumentException("BLE device must have been seen by at least one observer!");
        }

        LeastSquaresOptimizer.Optimum optimum = createOptimum(beacon.getMeasuredPower(), new ArrayList<>(), obs);

        // Location in form of [x,y].
        double[] centroid = optimum.getPoint().toArray();

        // Standard error in form of [x,y].
        double[] standardDeviation = optimum.getSigma(0).toArray();

        // Covariance matrix.
        RealMatrix covMatrix = optimum.getCovariances(0);

        // Alignment of principal eigenvector compared to x-axis.
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
        double arctan = Math.atan(asArray[0] / asArray[1]); // angle is gotten through arc tangent.

        return new Location2D(beacon.getId(),
                centroid[0], centroid[1],
                standardDeviation[0], standardDeviation[1], arctan);
    }

    /**
     * Calculates location of all the devices. Ignoring those that can't be solved.
     *
     * @param beacons List of devices that needs to be located.
     *
     * @see LocationService2D#calculateLocation(Beacon)
     *
     * @return Location of all the devices that can be pinpointed using trilateration.
     */
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
