package fi.helsinki.btls.domain;

/**
 * Model class for location data.
 */
public class LocationModel {
    private String beaconId;
    private double x;
    private double y;
    private double xr;
    private double yr;

    public LocationModel() {
    }

    public LocationModel(String beaconId, double x, double y, double xr, double yr) {
        this.beaconId = beaconId;
        this.x = x;
        this.y = y;
        this.xr = xr;
        this.yr = yr;
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

    @Override
    public String toString() {
        return "{ 'beaconId':'" + beaconId + "', 'x':" + x + ", 'y':" + y +
                ", 'xr':" + xr + ", 'yr':" + yr + " }";
    }
}
