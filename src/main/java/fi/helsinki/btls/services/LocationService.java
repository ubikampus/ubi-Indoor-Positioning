
package fi.helsinki.btls.services;

import java.util.List;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;

/**
 * Location service class.
 */
public class LocationService implements IMqttService{
    
    private String raspId;
    private String beaconId;
    private long volume;
    
    public LocationService(String raspId, String beaconId, long volume) {
        this.raspId = raspId;
        this.beaconId = beaconId;
        this.volume = volume;
    } 
    
    @Override
    public List<ObservationModel> getObservations() throws Exception {
        return null;
    }

    @Override
    public void publish(LocationModel locationModel) {
        
    }   
}
