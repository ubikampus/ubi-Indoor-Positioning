
package fi.helsinki.btls.services;

import java.util.List;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Location service class.
 */
public class LocationService {
    
    private IMqttService service;
    
    public LocationService(IMqttService service) {
        this.service = service;
    }

    public void calculateLocation() throws Exception {
        List<ObservationModel> obs = service.getObservations();
        double[][] positions = new double[obs.size()][2];
        double[] distances = new double[obs.size()];

        if (!obs.isEmpty()) {
            for (ObservationModel model : obs) {

            }

            //double[][] positions = new double[][] { { 5.0, -6.0 }, { 13.0, -15.0 }, { 21.0, -3.0 }, { 12.4, -21.2 } };
            //double[] distances = new double[] { 8.06, 13.97, 23.32, 15.31 };

            NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
            LeastSquaresOptimizer.Optimum optimum = solver.solve();

            // the answer
            double[] centroid = optimum.getPoint().toArray();

            // error and geometry information; may throw SingularMatrixException depending the threshold argument provided
            RealVector standardDeviation = optimum.getSigma(0);
            RealMatrix covarianceMatrix = optimum.getCovariances(0);



            LocationModel model = new LocationModel(obs.get(obs.size() - 1).getBeaconId(),
                    obs.get(obs.size() - 1).getVolume(),
                    obs.get(obs.size() - 1).getVolume(),
                    obs.get(obs.size() - 1).getVolume(),
                    obs.get(obs.size() - 1).getVolume());
            service.publish(model);
        }
    }
    

}
