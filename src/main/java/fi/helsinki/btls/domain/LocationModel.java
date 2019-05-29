package fi.helsinki.btls.domain;

/**
 * Model class for location data.
 */
public class LocationModel {
    private String beaconId;
    private double x;
    private double y;
    private double z;
    private double xr;
    private double yr;
    private double zr;

    public LocationModel() {
    }

    public LocationModel(String beaconId, double x, double y, double z, double xr, double yr, double zr) {
        this.beaconId = beaconId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xr = xr;
        this.yr = yr;
        this.zr = zr;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getXr() {
        return xr;
    }

    public void setXr(double xr) {
        this.xr = xr;
    }

    public double getYr() {
        return yr;
    }

    public void setYr(double yr) {
        this.yr = yr;
    }

    public double getZr() {
        return zr;
    }

    public void setZr(double zr) {
        this.zr = zr;
    }

    @Override
    public String toString() {
        return "{ 'beaconId':'" + beaconId + "', 'x':" + x + ", 'y':" + y + ", 'z':" + z +
                ", 'xr':" + xr + ", 'yr':" + yr + ", 'zr':" + zr + " }";
    }
}
