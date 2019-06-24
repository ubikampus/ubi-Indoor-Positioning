package fi.helsinki.btls.datamodels;

import lombok.*;

/**
 * Representation of specific beacons location on two dimensional space.
 * With information about locations standard error.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Location {
    protected String beaconId;
    protected double x;
    protected double y;
    protected double xr;
    protected double yr;
}
