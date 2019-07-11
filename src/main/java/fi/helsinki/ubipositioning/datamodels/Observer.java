package fi.helsinki.ubipositioning.datamodels;

import java.util.Objects;

/**
 * Representation for BLE listener. Contains the listeners name and its static location.
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
        if (o == this) {
            return true;
        }

        if (!(o instanceof Observer)) {
            return false;
        }

        final Observer other = (Observer) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }

        final Object thisObserverId = this.getObserverId();
        final Object otherObserverId = other.getObserverId();
        if (!Objects.equals(thisObserverId, otherObserverId)) {
            return false;
        }

        return java.util.Arrays.equals(this.getPosition(), other.getPosition());
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Observer;
    }

    public int hashCode() {
        final int prime = 59;
        int result = 1;
        final Object thisObserverId = this.getObserverId();
        result = result * prime + (thisObserverId == null ? 43 : thisObserverId.hashCode());
        result = result * prime + java.util.Arrays.hashCode(this.getPosition());
        return result;
    }

    public String toString() {
        return "Observer(observerId=" + this.getObserverId() +
                ", position=" + java.util.Arrays.toString(this.getPosition()) + ")";
    }
}
