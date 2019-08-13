package fi.helsinki.ubipositioning.trilateration;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.linear.RealMatrix;
import java.util.*;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import fi.helsinki.ubipositioning.datamodels.*;
import fi.helsinki.ubipositioning.datamodels.Observer;
import fi.helsinki.ubipositioning.utils.*;

/**
 * Base logic for trilateration calculation that finds solution if possible in n-dimensional space.
 */
public class LocationService implements ILocationService {
    private IObserverService observerService;
    private IMeasurementFilter filter;
    private ISignalMapper signalMapper;
    private IResultConverter resultConverter;

    /**
     * Creates instance of the class.
     *
     * @param observerService service to store and handle all the observers.
     * @param signalMapper converts rssi into distance.
     * @param resultConverter converter to create proper model out of result.
     * @param filter filter to smooth out inaccuracy of raw data.
     */
    public LocationService(IObserverService observerService, ISignalMapper signalMapper,
                           IResultConverter resultConverter, IMeasurementFilter filter) {
        this.observerService = observerService;
        this.filter = filter;
        this.signalMapper = signalMapper;
        this.resultConverter = resultConverter;
    }

    /**
     * Creates instance of the class using default measurement filter.
     *
     * @see MeasurementFilter
     *
     * @param observerService service to store and handle all the observers.
     * @param signalMapper converts rssi into distance.
     * @param resultConverter converter to create proper model out of result.
     */
    public LocationService(IObserverService observerService, ISignalMapper signalMapper,
                           IResultConverter resultConverter) {
        this(observerService, signalMapper, resultConverter, new MeasurementFilter());
    }

    /**
     * Calculates the location of given device in n dimensional space using trilateration.
     *
     * @param beacon Device which location wanted to be known.
     *
     * @throws org.apache.commons.math3.linear.SingularMatrixException if covariance doesn't have solution.
     * @throws IllegalArgumentException if signal strength values are from less then two observers.
     * @throws UnknownDeviceException if beacon hasn't been detected by BLE listeners yet.
     * @throws UnknownDeviceException if observer is not configured into ObserverService.
     *
     * @return Location of given device.
     */
    @Override
    public Location calculateLocation(Beacon beacon) {
        List<Observation> obs = beacon.getObservations();

        if (obs.isEmpty()) {
            throw new UnknownDeviceException("BLE device must have been seen by at least one observer!");
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
            Observer observer = observerService.getObserver(val.getKey());

            if (observer == null) {
                throw new UnknownDeviceException("Observer not configured!");
            }

            dist.add(signalMapper.convert(smooth, beacon.getMeasuredPower()));
            pos.add(observer.getPosition());
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

        // Convert result into proper model.
        return resultConverter.convert(beacon, centroid, standardDeviation, covMatrix);
    }
}
