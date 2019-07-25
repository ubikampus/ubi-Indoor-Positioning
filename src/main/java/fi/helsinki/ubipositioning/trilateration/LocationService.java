package fi.helsinki.ubipositioning.trilateration;

import fi.helsinki.ubipositioning.utils.Kalman;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import java.util.*;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import fi.helsinki.ubipositioning.datamodels.*;
import fi.helsinki.ubipositioning.utils.IObserverService;
import fi.helsinki.ubipositioning.utils.PathLossModel;

import javax.print.attribute.HashAttributeSet;

/**
 * Base logic for trilateration calculation that finds solution if possible in n-dimensional space.
 */
abstract class LocationService implements ILocationService {
    IObserverService iObserverService;
    PathLossModel pathLossModel;

    public LocationService(PathLossModel pathLossModel) {
        this.pathLossModel = pathLossModel;
    }


    /**
     * Sets environmental factor to half way of possible values so that rssi distance conversation is possible.
     * also sets false that rssi would equal distance.
     */

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
        HashMap<String, List<Double>> measurements = new HashMap();
        Kalman k = new Kalman();

        // Go through observations from newest to latest.
        for (int i = obs.size() - 1; i >= 0; i--) {
            Observation model = obs.get(i);

            if (!measurements.containsKey(model.getRaspId())) {
                measurements.put(model.getRaspId(), new ArrayList<>());
            }
            measurements.get(model.getRaspId()).add(model.getRssi());

            // preventing double value from observers.
            //if (!observersChecked.contains(model.getRaspId())) {
            //    dist.add(pathLossModel.calculateDistance(model.getRssi(), measuredPower)); // changing rssi into millimeters distance.
            //    pos.add(iObserverService.getObserver(model.getRaspId()).getPosition());
            //    observersChecked.add(model.getRaspId());
            //}
        }
        for (Map.Entry<String, List<Double>> val :
                measurements.entrySet()) {
            dist.add(pathLossModel.calculateDistance(k.Calculate(val.getValue().toArray(new Double[0])), measuredPower)); // changing rssi into millimeters distance.
            pos.add(iObserverService.getObserver(val.getKey()).getPosition());
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
}
