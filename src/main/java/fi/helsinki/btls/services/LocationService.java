
package fi.helsinki.btls.services;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;
import fi.helsinki.btls.utils.PropertiesHandler;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

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

    public void calculateLocation() throws Exception {
        List<ObservationModel> obs = service.getObservations();
        double[][] positions = new double[obs.size()][3]; // 3-D positions as x,y,z of observation posts
        double[] distances = new double[obs.size()]; // disatcnes of the beacon from observation post which coordinates are in same index in positions array

        if (!obs.isEmpty()) {
            for (int i = 0; i < obs.size(); i++) {
                ObservationModel model = obs.get(i);
                String[] rasp = rasps.get(model.getRaspId()).split(":");
                positions[i][0] = Double.parseDouble(rasp[0]);
                positions[i][1] = Double.parseDouble(rasp[1]);
                positions[i][2] = Double.parseDouble(rasp[2]);

                distances[i] = model.getVolume();
            }

            NonLinearLeastSquaresSolver solver;
            solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
            LeastSquaresOptimizer.Optimum optimum = solver.solve();

            // gotten location in form of [x,y,z]
            double[] centroid = optimum.getPoint().toArray();

            // error and geometry information; may throw SingularMatrixException depending the threshold argument provided
            RealVector standardDeviation = optimum.getSigma(0);
            RealMatrix covarianceMatrix = optimum.getCovariances(0);

            LocationModel model = new LocationModel(obs.get(obs.size() - 1).getBeaconId(),
                    centroid[0], centroid[1], centroid[2],
                    obs.get(obs.size() - 1).getVolume(),
                    obs.get(obs.size() - 1).getVolume(),
                    obs.get(obs.size() - 1).getVolume());
            service.publish(model);
        }
    }
    

}
