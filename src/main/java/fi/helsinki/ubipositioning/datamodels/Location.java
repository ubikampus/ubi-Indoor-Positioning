package fi.helsinki.ubipositioning.datamodels;

import java.util.Objects;

/**
 * Representation of specific location on two dimensional space
 * with information about locations standard error.
 */
public class Location {
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
        if (o == this) {
            return true;
        }
        
        if (!(o instanceof Location)) {
            return false;
        }
        
        final Location other = (Location) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        
        final Object thisBeaconId = this.getBeaconId();
        final Object otherBeaconId = other.getBeaconId();
        if (!Objects.equals(thisBeaconId, otherBeaconId)) {
            return false;
        }
        
        if (Double.compare(this.getX(), other.getX()) != 0) {
            return false;
        }
        
        if (Double.compare(this.getY(), other.getY()) != 0) {
            return false;
        }
        
        if (Double.compare(this.getXr(), other.getXr()) != 0) {
            return false;
        }
        
        return Double.compare(this.getYr(), other.getYr()) == 0;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Location;
    }

    public int hashCode() {
        final int prime = 59;
        int result = 1;
        final Object thisBeaconId = this.getBeaconId();
        result = result * prime + (thisBeaconId == null ? 43 : thisBeaconId.hashCode());
        final long thisX = Double.doubleToLongBits(this.getX());
        result = result * prime + (int) (thisX >>> 32 ^ thisX);
        final long thisY = Double.doubleToLongBits(this.getY());
        result = result * prime + (int) (thisY >>> 32 ^ thisY);
        final long thisXr = Double.doubleToLongBits(this.getXr());
        result = result * prime + (int) (thisXr >>> 32 ^ thisXr);
        final long thisYr = Double.doubleToLongBits(this.getYr());
        result = result * prime + (int) (thisYr >>> 32 ^ thisYr);
        return result;
    }

    public String toString() {
        return "Location(beaconId=" + this.getBeaconId() + ", x=" + this.getX() +
                ", y=" + this.getY() + ", xr=" + this.getXr() +
                ", yr=" + this.getYr() + ")";
    }
}
