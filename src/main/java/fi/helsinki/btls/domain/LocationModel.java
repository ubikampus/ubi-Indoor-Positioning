package fi.helsinki.btls.domain;

/**
 * Model class for location data.
 */
public class LocationModel {
    private String beaconId;
    private long x;
    private long y;
    private long xr;
    private long yr;

    public LocationModel() {
    }

    public LocationModel(String beaconId, long x, long y, long xr, long yr) {
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

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    public long getXr() {
        return xr;
    }

    public void setXr(long xr) {
        this.xr = xr;
    }

    public long getYr() {
        return yr;
    }

    public void setYr(long yr) {
        this.yr = yr;
    }

    @Override
    public String toString() {
        return "{ 'beaconId':'" + beaconId + "', 'x':" + x + ", 'y':" + y +
                ", 'xr':" + xr + ", 'yr':" + yr + " }";
    }
}
