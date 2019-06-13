package fi.helsinki.btls.domain;

import lombok.*;

/**
 * Representation of specific beacons location on two dimensional space.
 * With information about locations standard error and the error ellipse alignment against x-axis.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Location2D extends LocationModel {
    private double alignment;

    public Location2D(String beaconId, double x, double y, double xr, double yr, double alignment) {
        super(beaconId, x, y, xr, yr);
        this.alignment = alignment;
    }

    /**
     * Method to produce JSON representation of the objects.
     *
     * @return JSON string containing all the the objects data as json object.
     */
    @Override
    public String toJson() {
        return "{ \"beaconId\": \"" + beaconId + "\", \"x\": " + x + ", \"y\": " + y +
                ", \"xr\": " + xr + ", \"yr\": " + yr + ", \"alignment\": " + alignment + " }";
    }
}
