package fi.helsinki.btls.services;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import java.util.*;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import fi.helsinki.btls.domain.*;

/**
 * Interface for location calculation service.
 */
public interface ILocationService {
    LocationModel calculateLocation(Beacon beacon);
    List<LocationModel> calculateAllLocations(List<Beacon> beacons);


    void deleteAllRasps();

    /**
     * Static method to create solution for trilaterion task.
     *
     * @param beacon which devices location is been calculated.
     * @param raspsChecked rasps that have been already gone through.
     * @param obs observations related to beacon.
     * @param rasps existing observation posts.
     *
     * @return Optimum object which contains the result for problem in raw form.
     */
    static LeastSquaresOptimizer.Optimum createOptimum(Beacon beacon, List<String> raspsChecked,
                                                       List<ObservationModel> obs, Map<String, double[]> rasps) {
        List<double[]> pos = new ArrayList<>();
        List<Double> dist = new ArrayList<>();

        // Go through observations from newest to latest
        for (int i = obs.size() - 1; i >= 0; i--) {
            ObservationModel model = obs.get(i);

            // preventing double value for rasps
            if (!raspsChecked.contains(model.getRaspId())) {
                dist.add(Math.abs(model.getVolume()) - beacon.getMinVolume()); // Needs scaling on minimum value of RSSI.
                pos.add(rasps.get(model.getRaspId()));
                raspsChecked.add(model.getRaspId());
            }
        }

        double[][] positions = new double[pos.size()][pos.get(0).length]; // Positions of observation posts.
        double[] distances = new double[dist.size()]; // Distances from observation post that's coordinates are in same index in positions array.

        // Move from ArrayList to array so that empty slots can be avoided.
        // Otherwise covariance throws singularity exception as it doesn't allow same value to be present at same column in different rows
        // at least if this includes all the rows same time. Not sure about other scenarios.
        for (int i = 0; i < dist.size(); i++) {
            positions[i] = pos.get(i);
            distances[i] = dist.get(i);
        }

        NonLinearLeastSquaresSolver solver;
        solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
        return solver.solve();
    }
}
