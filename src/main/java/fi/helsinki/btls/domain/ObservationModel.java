package fi.helsinki.btls.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class for observation data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObservationModel {
    private String raspId;
    private String beaconId;
    private double rssi;
}
