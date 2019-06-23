package fi.helsinki.btls.datamodels;

/**
 * Representation of specific beacons location on two dimensional space.
 * With information about locations standard error.
 */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public abstract class Location {
    protected String beaconId;
    protected double x;
    protected double y;
    protected double xr;
    protected double yr;

    public Location(String beaconId, double x, double y, double xr, double yr) {
        this.beaconId = beaconId;
        this.x = x;
        this.y = y;
        this.xr = xr;
        this.yr = yr;
    }

    public Location() {
    }

    public String getBeaconId() {
        return this.beaconId;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getXr() {
        return this.xr;
    }

    public double getYr() {
        return this.yr;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setXr(double xr) {
        this.xr = xr;
    }

    public void setYr(double yr) {
        this.yr = yr;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Location)) return false;
        final Location other = (Location) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$beaconId = this.getBeaconId();
        final Object other$beaconId = other.getBeaconId();
        if (this$beaconId == null ? other$beaconId != null : !this$beaconId.equals(other$beaconId)) return false;
        if (Double.compare(this.getX(), other.getX()) != 0) return false;
        if (Double.compare(this.getY(), other.getY()) != 0) return false;
        if (Double.compare(this.getXr(), other.getXr()) != 0) return false;
        if (Double.compare(this.getYr(), other.getYr()) != 0) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Location;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $beaconId = this.getBeaconId();
        result = result * PRIME + ($beaconId == null ? 43 : $beaconId.hashCode());
        final long $x = Double.doubleToLongBits(this.getX());
        result = result * PRIME + (int) ($x >>> 32 ^ $x);
        final long $y = Double.doubleToLongBits(this.getY());
        result = result * PRIME + (int) ($y >>> 32 ^ $y);
        final long $xr = Double.doubleToLongBits(this.getXr());
        result = result * PRIME + (int) ($xr >>> 32 ^ $xr);
        final long $yr = Double.doubleToLongBits(this.getYr());
        result = result * PRIME + (int) ($yr >>> 32 ^ $yr);
        return result;
    }

    public String toString() {
        return "Location(beaconId=" + this.getBeaconId() + ", x=" + this.getX() + ", y=" + this.getY() + ", xr=" + this.getXr() + ", yr=" + this.getYr() + ")";
    }
}
