package fi.helsinki.ubipositioning.datamodels;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Wrapper for individual observation collected by listener about BLE device.
 */
public class Observation {
    private String observerId;
    private String beaconId;
    private double rssi;
    private LocalDateTime timestamp;

    public Observation(String observerId, String beaconId, double rssi) {
        this(observerId, beaconId, rssi, LocalDateTime.now());
    }

    public Observation(String observerId, String beaconId, double rssi, LocalDateTime timestamp) {
        this.observerId = observerId;
        this.beaconId = beaconId;
        this.rssi = rssi;
        this.timestamp = timestamp;
    }

    public Observation() {
    }

    public String getObserverId() {
        return this.observerId;
    }

    public String getBeaconId() {
        return this.beaconId;
    }

    public double getRssi() {
        return this.rssi;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public void setObserverId(String observerId) {
        this.observerId = observerId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public void setRssi(double rssi) {
        this.rssi = rssi;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        
        if (!(o instanceof Observation)) {
            return false;
        }
        
        final Observation other = (Observation) o;
        if (!other.canEqual((Object) this)) {
            return false;
        }
        
        final Object thisObserverId = this.getObserverId();
        final Object otherObserverId = other.getObserverId();
        if (!Objects.equals(thisObserverId, otherObserverId)) {
            return false;
        }
        
        final Object thisBeaconId = this.getBeaconId();
        final Object otherBeaconId = other.getBeaconId();
        if (!Objects.equals(thisBeaconId, otherBeaconId)) {
            return false;
        }
        
        if (Double.compare(this.getRssi(), other.getRssi()) != 0) {
            return false;
        }
        
        final Object thisTimestamp = this.getTimestamp();
        final Object otherTimestamp = other.getTimestamp();
        return Objects.equals(thisTimestamp, otherTimestamp);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Observation;
    }

    public int hashCode() {
        final int prime = 59;
        int result = 1;
        final Object thisObserverId = this.getObserverId();
        result = result * prime + (thisObserverId == null ? 43 : thisObserverId.hashCode());
        final Object thisBeaconId = this.getBeaconId();
        result = result * prime + (thisBeaconId == null ? 43 : thisBeaconId.hashCode());
        final long thisRssi = Double.doubleToLongBits(this.getRssi());
        result = result * prime + (int) (thisRssi >>> 32 ^ thisRssi);
        final Object thisTimestamp = this.getTimestamp();
        result = result * prime + (thisTimestamp == null ? 43 : thisTimestamp.hashCode());
        return result;
    }

    public String toString() {
        return "Observation(observerId=" + this.getObserverId() + ", beaconId=" + this.getBeaconId() +
                ", rssi=" + this.getRssi() + ", timestamp=" + this.getTimestamp() + ")";
    }
}
