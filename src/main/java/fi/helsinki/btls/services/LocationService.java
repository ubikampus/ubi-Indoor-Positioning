
package fi.helsinki.btls.services;

public class LocationService {
    
    private String raspId;
    private String beaconId;
    private long volume;
    
    public LocationService(String raspId, String beaconId, long volume) {
        this.raspId = raspId;
        this.beaconId = beaconId;
        this.volume = volume;
    } 
    
    public void calculateLocation() {
        
    }
   
}
