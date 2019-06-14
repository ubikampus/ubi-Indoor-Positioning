package fi.helsinki.btls.domain;

/**
 * Data model for observers location.
 */
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
        return observerId;
    }

    public void setObserverId(String observerId) {
        this.observerId = observerId;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }
}
