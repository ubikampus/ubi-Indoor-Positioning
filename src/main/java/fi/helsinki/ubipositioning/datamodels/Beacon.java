package fi.helsinki.ubipositioning.datamodels;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Model for BLE device. Wraps up devices name, lifetime,
 * data as observations that has been collected about it
 * and it's measured power when in 1 meter distances of device that it's listening.
 *
 * @see Observation
 */
public class Beacon {
    private String id;
    private double measuredPower;
    private List<Observation> observations;
    private int maxLifetime;

    public Beacon(String id) {
        this(id, 30);
    }

    public Beacon(String id, int maxLifetime) {
        this.id = id;
        this.measuredPower = -Double.MAX_VALUE;
        this.observations = new ArrayList<>();
        this.maxLifetime = maxLifetime;
    }

    public Beacon(String id, double measuredPower, List<Observation> observations) {
        this.id = id;
        this.measuredPower = measuredPower;
        this.observations = observations;
    }

    public Beacon(String id, double measuredPower, List<Observation> observations, int maxLifetime) {
        this.id = id;
        this.measuredPower = measuredPower;
        this.observations = observations;
        this.maxLifetime = maxLifetime;
    }

    public Beacon() {
    }

    public List<Observation> getObservations() {
        observations = observations
                .stream()
                .filter(x -> x.getTimestamp().isAfter(LocalDateTime.now().minusSeconds(maxLifetime)))
                .collect(Collectors.toList());
        return observations;
    }


    /**
     * Set observations of the beacon.
     * Also updates the min RSSI to equal the new list of observations if needed.
     *
     * @param observations list of observations.
     */
    public void setObservations(List<Observation> observations) {
        this.observations = observations;

        if (!observations.isEmpty()) {
            double measuredPower = observations
                    .stream()
                    .map(Observation::getRssi)
                    .max(Comparator.comparing(Double::valueOf))
                    .get();

            if (measuredPower > this.measuredPower) {
                this.measuredPower = measuredPower;
            }
        }
    }

    public String getId() {
        return this.id;
    }

    public double getMeasuredPower() {
        return this.measuredPower;
    }

    public int getMaxLifetime() {
        return this.maxLifetime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMeasuredPower(double measuredPower) {
        this.measuredPower = measuredPower;
    }

    public void setMaxLifetime(int maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Beacon)) {
            return false;
        }

        final Beacon other = (Beacon) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        
        final Object thisId = this.getId();
        final Object otherId = other.getId();
        if (!Objects.equals(thisId, otherId)) {
            return false;
        }
        
        if (Double.compare(this.getMeasuredPower(), other.getMeasuredPower()) != 0) {
            return false;
        }
        
        final Object thisObservations = this.getObservations();
        final Object otherObservations = other.getObservations();
        if (!Objects.equals(thisObservations, otherObservations)) {
            return false;
        }

        return this.getMaxLifetime() == other.getMaxLifetime();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Beacon;
    }

    public int hashCode() {
        final int prime = 59;
        int result = 1;
        final Object thisId = this.getId();
        result = result * prime + (thisId == null ? 43 : thisId.hashCode());
        final long thisMeasuredPower = Double.doubleToLongBits(this.getMeasuredPower());
        result = result * prime + (int) (thisMeasuredPower >>> 32 ^ thisMeasuredPower);
        final Object thisObservations = this.getObservations();
        result = result * prime + (thisObservations == null ? 43 : thisObservations.hashCode());
        result = result * prime + this.getMaxLifetime();
        return result;
    }

    public String toString() {
        return "Beacon(id=" + this.getId() + ", measuredPower=" + this.getMeasuredPower() +
                ", observations=" + this.getObservations() +
                ", maxLifetime=" + this.getMaxLifetime() + ")";
    }
}
