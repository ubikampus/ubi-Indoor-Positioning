package fi.helsinki.ubipositioning.trilateration;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import java.util.*;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import fi.helsinki.ubipositioning.datamodels.*;
import fi.helsinki.ubipositioning.utils.IObserverService;

/**
 * Base logic for trilateration calculation that finds solution if possible in n-dimensional space.
 */
abstract class LocationService implements ILocationService {
    IObserverService iObserverService;
    private boolean calculateDistance;
    private int environmentalFactor;

    /**
     * Sets environmental factor to half way of possible values so that rssi distance conversation is possible.
     * also sets false that rssi would equal distance.
     */
    LocationService() {
        this.calculateDistance = false;
        this.environmentalFactor = 2;
    }

    /**
     * Create solution for trilateration task.
     *
     * @param measuredPower Base power of BLE listener.
     * @param observersChecked Observers to ignore because are already checked or irrelevant.
     * @param obs Data on which position localization is done.
     *
     * @throws org.apache.commons.math3.linear.SingularMatrixException if covariance doesn't have solution.
     * @throws IllegalArgumentException if signal strength values are from less then two observers.
     *
     * @return Optimum object which contains the result for problem in raw form.
     */
    LeastSquaresOptimizer.Optimum createOptimum(double measuredPower, List<String> observersChecked,
                                                List<Observation> obs) {
        List<double[]> pos = new ArrayList<>();
        List<Double> dist = new ArrayList<>();

        // Go through observations from newest to latest.
        for (int i = obs.size() - 1; i >= 0; i--) {
            Observation model = obs.get(i);

            // preventing double value from observers.
            if (!observersChecked.contains(model.getRaspId())) {
                dist.add(getDistanceFromRssi(model.getRssi(), measuredPower)); // changing rssi into millimeters distance.
                pos.add(iObserverService.getObserver(model.getRaspId()).getPosition());
                observersChecked.add(model.getRaspId());
            }
        }

        double[][] positions = new double[pos.size()][pos.get(0).length]; // Positions of observers (BLE listeners).
        double[] distances = new double[dist.size()]; // Distances from each observer to target BLE device.

        // Move from ArrayList to array so that empty slots can be avoided.
        // Otherwise covariance throws singularity exception
        // as there is no solution if one dimension contains multiple same values.
        for (int i = 0; i < dist.size(); i++) {
            positions[i] = pos.get(i);
            distances[i] = dist.get(i);
        }

        NonLinearLeastSquaresSolver solver;
        solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
        return solver.solve();
    }

    public void setCalculateDistance(boolean calculateDistance) {
        this.calculateDistance = calculateDistance;
    }

    /**
     * Converts rssi value into distance of millimeters.
     *
     * @param rssi signal strength that BLE listener has picked up..
     * @param measuredPower Base power of BLE listener.
     *
     * @return distance in millimeters of device which rssi is given.
     */
    public double getDistanceFromRssi(double rssi, double measuredPower) {
        if (this.calculateDistance) {
            return Math.pow(10, (measuredPower - rssi) / (10 * this.environmentalFactor)) * 1000;
        } else {
            return rssi - measuredPower;
        }
    }

    public int getEnvironmentalFactor() {
        return environmentalFactor;
    }

    public void setEnvironmentalFactor(int environmentalFactor) {
        this.environmentalFactor = environmentalFactor;
    }
}
