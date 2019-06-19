package fi.helsinki.btls.datamodels;

import lombok.*;

/**
 * Representation of specific beacons location on two dimensional space.
 * With information about locations standard error and the error ellipse alignment against x-axis.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Location2D extends Location {
    private double alignment;

    public Location2D(String beaconId, double x, double y, double xr, double yr, double alignment) {
        super(beaconId, x, y, xr, yr);
        this.alignment = alignment;
    }
}
