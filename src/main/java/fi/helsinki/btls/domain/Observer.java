package fi.helsinki.btls.domain;

import lombok.Data;

/**
 * Data model for observers location.
 */
@Data
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
}
