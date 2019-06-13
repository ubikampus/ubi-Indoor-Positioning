package fi.helsinki.btls.domain;

import lombok.*;

/**
 * Representation of specific beacons location on two dimensional space.
 * With information about locations standard error.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class LocationModel {
    protected String beaconId;
    protected double x;
    protected double y;
    protected double xr;
    protected double yr;

    /**
     * Method to produce JSON representation of the objects.
     *
     * @return JSON string containing all the the objects data as json object.
     */
    public abstract String toJson();
}
