
package fi.helsinki.btls.services;

import java.util.List;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;

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
        if (obs == null || obs.isEmpty()) {
            service.publish(new LocationModel("beacon", 1, 1, 1, 1));
        }
        LocationModel model = new LocationModel(obs.get(obs.size() - 1).getBeaconId(),
                obs.get(obs.size() - 1).getVolume(),
                obs.get(obs.size() - 1).getVolume(),
                obs.get(obs.size() - 1).getVolume(),
                obs.get(obs.size() - 1).getVolume());
        service.publish(model);
    }
    

}
