package fi.helsinki.btls.datamodels;

/**
 * Data model for observers location.
 */
//@Data
public class Observer {
    private String observerId;
    private double[] position;

    public Observer(String observerId) {
        this.observerId = observerId;
    }

    public Observer(String observerId, double x, double y) {
        this.observerId = observerId;
        position = new double[]{x, y};
    }

    public Observer(String observerId, double x, double y, double z) {
        this.observerId = observerId;
        position = new double[]{x, y, z};
    }

    public String getObserverId() {
        return this.observerId;
    }

    public double[] getPosition() {
        return this.position;
    }

    public void setObserverId(String observerId) {
        this.observerId = observerId;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Observer)) return false;
        final Observer other = (Observer) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$observerId = this.getObserverId();
        final Object other$observerId = other.getObserverId();
        if (this$observerId == null ? other$observerId != null : !this$observerId.equals(other$observerId))
            return false;
        if (!java.util.Arrays.equals(this.getPosition(), other.getPosition())) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Observer;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $observerId = this.getObserverId();
        result = result * PRIME + ($observerId == null ? 43 : $observerId.hashCode());
        result = result * PRIME + java.util.Arrays.hashCode(this.getPosition());
        return result;
    }

    public String toString() {
        return "Observer(observerId=" + this.getObserverId() + ", position=" + java.util.Arrays.toString(this.getPosition()) + ")";
    }
}
