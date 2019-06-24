package fi.helsinki.btls.datamodels;

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
    private String raspId;
    private String beaconId;
    private double rssi;
}
