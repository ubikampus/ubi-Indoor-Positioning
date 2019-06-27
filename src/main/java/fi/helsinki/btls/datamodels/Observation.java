package fi.helsinki.btls.datamodels;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class for observation data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Observation {
    public Observation(String raspId, String beaconId, double rssi) {
        this.raspId = raspId;
        this.beaconId = beaconId;
        this.rssi = rssi;
        this.timestamp = LocalDateTime.now();
    }

    private String raspId;
    private String beaconId;
    private double rssi;
    private LocalDateTime timestamp;
}
