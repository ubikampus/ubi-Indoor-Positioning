
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

    public void CalculateLocation() throws Exception {
        List<ObservationModel> obs = service.getObservations();
        service.publish(new LocationModel("beacon", 1, 1, 1, 1));
    }
    

}
