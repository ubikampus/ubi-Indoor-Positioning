
package fi.helsinki.btls.services;

import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;
import java.util.List;

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
    public List<ObservationModel> getObsercations() throws Exception {
        return null;
    }

    @Override
    public void publish(LocationModel locationModel) {
        
    }   
}
