package fi.helsinki.ubipositioning.datamodels;

/**
 * Representation of specific beacons location on two dimensional space.
 * With information about locations standard error and the error ellipse alignment against x-axis.
 */
//@Data
//@NoArgsConstructor
//@EqualsAndHashCode(callSuper = true)
//@ToString(callSuper = true)
public class Location2D extends Location {
    private double alignment;

    public Location2D(String beaconId, double x, double y, double xr, double yr, double alignment) {
        super(beaconId, x, y, xr, yr);
        this.alignment = alignment;
    }

    public Location2D() {
    }

    public double getAlignment() {
        return this.alignment;
    }

    public void setAlignment(double alignment) {
        this.alignment = alignment;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Location2D)) {
            return false;
        }

        final Location2D other = (Location2D) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }

        if (!super.equals(o)) {
            return false;
        }

        return Double.compare(this.getAlignment(), other.getAlignment()) == 0;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Location2D;
    }

    public int hashCode() {
        final int prime = 59;
        int result = super.hashCode();
        final long thisAlignment = Double.doubleToLongBits(this.getAlignment());
        result = result * prime + (int) (thisAlignment >>> 32 ^ thisAlignment);
        return result;
    }

    public String toString() {
        return "Location2D(super=" + super.toString() + ", alignment=" + this.getAlignment() + ")";
    }
}
