package fi.helsinki.ubipositioning.datamodels;

/**
 * Representation of specific beacons location on three dimensional space.
 * With information about locations standard error.
 */
//@Data
//@NoArgsConstructor
//@EqualsAndHashCode(callSuper = true)
//@ToString(callSuper = true)
public class Location3D extends Location {
    private double z;
    private double zr;

    public Location3D(String beaconId, double x, double y, double z, double xr, double yr, double zr) {
        super(beaconId, x, y, xr, yr);
        this.z = z;
        this.zr = zr;
    }

    public Location3D() {
    }

    public double getZ() {
        return this.z;
    }

    public double getZr() {
        return this.zr;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setZr(double zr) {
        this.zr = zr;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        
        if (!(o instanceof Location3D)) {
            return false;
        }
        
        final Location3D other = (Location3D) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        
        if (!super.equals(o)) {
            return false;
        }
        
        if (Double.compare(this.getZ(), other.getZ()) != 0) {
            return false;
        }

        return Double.compare(this.getZr(), other.getZr()) == 0;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Location3D;
    }

    public int hashCode() {
        final int prime = 59;
        int result = super.hashCode();
        final long thisZ = Double.doubleToLongBits(this.getZ());
        result = result * prime + (int) (thisZ >>> 32 ^ thisZ);
        final long thisZr = Double.doubleToLongBits(this.getZr());
        result = result * prime + (int) (thisZr >>> 32 ^ thisZr);
        return result;
    }

    public String toString() {
        return "Location3D(super=" + super.toString() + ", z=" + this.getZ() + ", zr=" + this.getZr() + ")";
    }
}
