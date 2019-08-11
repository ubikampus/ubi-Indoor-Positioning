package fi.helsinki.ubipositioning.trilateration;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.linear.RealMatrix;
import java.util.*;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import fi.helsinki.ubipositioning.datamodels.*;
import fi.helsinki.ubipositioning.utils.*;

/**
 * Base logic for trilateration calculation that finds solution if possible in n-dimensional space.
 */
public class LocationService implements ILocationService {
    private IObserverService observerService;
    private IMeasurementFilter filter;
    private IDataConverter dataConverter;
    private IResultConverter resultConverter;

    public LocationService(IObserverService observerService, IDataConverter dataConverter,
                           IResultConverter resultConverter, IMeasurementFilter filter) {
        this.observerService = observerService;
        this.filter = filter;
        this.dataConverter = dataConverter;
        this.resultConverter = resultConverter;
    }

    public LocationService(IObserverService observerService, IDataConverter dataConverter,
                           IResultConverter resultConverter) {
        this(observerService, dataConverter, resultConverter, new MeasurementFilter());
    }

    /**
     * Calculates the location of given device in three dimensional space using trilateration.
     *
     * @param beacon Device which location wanted to be known.
     *
     * @throws org.apache.commons.math3.linear.SingularMatrixException if covariance doesn't have solution.
     * @throws IllegalArgumentException if signal strength values are from less then two observers.
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

        List<double[]> pos = new ArrayList<>();
        List<Double> dist = new ArrayList<>();
        Map<String, List<Double>> measurements = new HashMap<>();

        // Go through observations from newest to latest.
        for (int i = obs.size() - 1; i >= 0; i--) {
            Observation model = obs.get(i);

            if (!measurements.containsKey(model.getObserverId())) {
                measurements.put(model.getObserverId(), new ArrayList<>());
            }

            measurements.get(model.getObserverId()).add(model.getRssi());
        }

        // Populate the arrays with static locations and with their distances to target.
        // Also inaccuracy is filtered out.
        for (Map.Entry<String, List<Double>> val : measurements.entrySet()) {
            Double[] measurementsArray = val.getValue().toArray(new Double[0]);
            double smooth = filter.smooth(measurementsArray);

            dist.add(dataConverter.convert(smooth, beacon.getMeasuredPower()));
            pos.add(observerService.getObserver(val.getKey()).getPosition());
        }

        double[][] positions = new double[pos.size()][pos.get(0).length]; // Positions of observers.
        double[] distances = new double[dist.size()]; // Distances from each observer to target.

        // Move from ArrayList to array so that empty slots can be avoided.
        // Otherwise covariance throws singularity exception
        // as there is no solution if one dimension contains multiple same values.
        for (int i = 0; i < dist.size(); i++) {
            positions[i] = pos.get(i);
            distances[i] = dist.get(i);
        }

        NonLinearLeastSquaresSolver solver;
        solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
        LeastSquaresOptimizer.Optimum optimum = solver.solve();

        // Location in form of [x,y,z].
        double[] centroid = optimum.getPoint().toArray();

        // Standard error in form of [x,y,z].
        double[] standardDeviation = optimum.getSigma(0).toArray();

        // Covariance matrix.
        RealMatrix covMatrix = optimum.getCovariances(0);

        return resultConverter.convert(beacon, centroid, standardDeviation, covMatrix);
    }

    /**
     * Calculates location of all the devices. Ignoring those that can't be solved.
     *
     * @param beacons List of devices that needs to be located.
     *
     * @see LocationService#calculateLocation(Beacon)
     *
     * @return Location of all the devices that can be pinpointed using trilateration.
     */
    @Override
    public List<Location> calculateAllLocations(List<Beacon> beacons) {
        List<Location> list = new ArrayList<>();

        beacons.forEach(beacon -> {
            try {
                list.add(calculateLocation(beacon));
            } catch (Exception ignored) {
            }
        });

        return list;
    }
}
