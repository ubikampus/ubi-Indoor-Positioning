package fi.helsinki.btls.domain;

import lombok.*;

/**
 * Representation of specific beacons location on three dimensional space.
 * With information about locations standard error.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Location3D extends LocationModel {
    private double z;
    private double zr;

    public Location3D(String beaconId, double x, double y, double z, double xr, double yr, double zr) {
        super(beaconId, x, y, xr, yr);
        this.z = z;
        this.zr = zr;
    }

    /**
     * Method to produce JSON representation of the objects.
     *
     * @return JSON string containing all the the objects data as json object.
     */
    @Override
    public String toJson() {
        return "{ \"beaconId\": \"" + beaconId + "\", \"x\": " + x + ", \"y\": " + y + ", \"z\": " + z +
                ", \"xr\": " + xr + ", \"yr\": " + yr + ", \"zr\": " + zr + " }";
    }
}
