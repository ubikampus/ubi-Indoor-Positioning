
package fi.helsinki.btls.services;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import java.util.*;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;
import fi.helsinki.btls.utils.PropertiesHandler;

/**
 * Location service class.
 */
public class LocationService {
    private IMqttService service;
    private Map<String, String> rasps;

    public LocationService(IMqttService service) {
        rasps = new PropertiesHandler("config.properties").getAllProperties();
        this.service = service;
    }

    public void calculateLocation2D() {
        List<String> raspsChecked = new ArrayList<>();
        List<ObservationModel> obs = service.getObservations();

        if (!obs.isEmpty()) {
            List<double[]> pos = new ArrayList<>();
            List<Double> dist = new ArrayList<>();

            // go through observations from newest to latest
            for (int i = obs.size() - 1; i >= 0; i--) {
                ObservationModel model = obs.get(i);

                // preventing double value for rasps
                if (!raspsChecked.contains(model.getRaspId())) {
                    String[] rasp = rasps.get(model.getRaspId()).split(":");
                    double[] temp = new double[2];

                    temp[0] = Double.parseDouble(rasp[0]);
                    temp[1] = Double.parseDouble(rasp[1]);

                    dist.add(model.getVolume());
                    pos.add(temp);
                    raspsChecked.add(model.getRaspId());
                }
            }

            double[][] positions = new double[pos.size()][2]; // 2-D positions as x,y of observation posts
            double[] distances = new double[dist.size()]; // distances from observation post thats coordinates are in same index in positions array

            // Move from ArrayList to array so that empty slots can be avoided.
            // Otherwise covariance throws singularity exception as it doesn't allow same value to be present at same column in different rows
            // at least if this includes all the rows same time. Not sure about other scenarios.
            for (int i = 0; i < dist.size(); i++) {
                positions[i] = pos.get(i);
                distances[i] = dist.get(i);
            }

            NonLinearLeastSquaresSolver solver;
            solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
            LeastSquaresOptimizer.Optimum optimum = solver.solve();

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

            LocationModel model = new LocationModel("undefined",
                    centroid[0], centroid[1], 0,
                    standardDeviation[0], standardDeviation[1], 0, arctan);

            service.publish(model);
        }
    }

    public void calculateLocation3D() {
        List<String> raspsChecked = new ArrayList<>();
        List<ObservationModel> obs = service.getObservations();

        if (!obs.isEmpty()) {
            List<double[]> pos = new ArrayList<>();
            List<Double> dist = new ArrayList<>();

            // go through observations from newest to latest
            for (int i = obs.size() - 1; i >= 0; i--) {
                ObservationModel model = obs.get(i);

                // preventing double value for rasps
                if (!raspsChecked.contains(model.getRaspId())) {
                    String[] rasp = rasps.get(model.getRaspId()).split(":");
                    double[] temp = new double[3];

                    temp[0] = Double.parseDouble(rasp[0]);
                    temp[1] = Double.parseDouble(rasp[1]);
                    temp[2] = Double.parseDouble(rasp[2]);

                    dist.add(model.getVolume());
                    pos.add(temp);
                    raspsChecked.add(model.getRaspId());
                }
            }

            double[][] positions = new double[pos.size()][3]; // 3-D positions as x,y,z of observation posts
            double[] distances = new double[dist.size()]; // distances from observation post thats coordinates are in same index in positions array

            // Move from ArrayList to array so that empty slots can be avoided.
            // Otherwise covariance throws singularity exception as it doesn't allow same value to be present at same column in different rows
            // at least if this includes all the rows same time. Not sure about other scenarios.
            for (int i = 0; i < dist.size(); i++) {
                positions[i] = pos.get(i);
                distances[i] = dist.get(i);
            }

            NonLinearLeastSquaresSolver solver;
            solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
            LeastSquaresOptimizer.Optimum optimum = solver.solve();

            // gotten location in form of [x,y,z]
            double[] centroid = optimum.getPoint().toArray();

            // should be in form of [x,y,z]
            double[] standardDeviation = optimum.getSigma(0).toArray();

            //covariance matrix
            RealMatrix covMatrix = optimum.getCovariances(0);

            LocationModel model = new LocationModel("undefined",
                    centroid[0], centroid[1], centroid[2],
                    standardDeviation[0], standardDeviation[1], standardDeviation[2], 0);

            service.publish(model);
        }
    }
}
