package fi.helsinki.btls.datamodels;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Model class for observation data.
 */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class Observation {
    private String raspId;
    private String beaconId;
    private double rssi;
    private LocalDateTime timestamp;

    public Observation(String raspId, String beaconId, double rssi) {
        this.raspId = raspId;
        this.beaconId = beaconId;
        this.rssi = rssi;
        this.timestamp = LocalDateTime.now();
    }

    public Observation(String raspId, String beaconId, double rssi, LocalDateTime timestamp) {
        this.raspId = raspId;
        this.beaconId = beaconId;
        this.rssi = rssi;
        this.timestamp = timestamp;
    }

    public Observation() {
    }

    public String getRaspId() {
        return this.raspId;
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

    public void setRaspId(String raspId) {
        this.raspId = raspId;
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
        
        final Object thisRaspId = this.getRaspId();
        final Object otherRaspId = other.getRaspId();
        if (!Objects.equals(thisRaspId, otherRaspId)) {
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
        final Object thisRaspId = this.getRaspId();
        result = result * prime + (thisRaspId == null ? 43 : thisRaspId.hashCode());
        final Object thisBeaconId = this.getBeaconId();
        result = result * prime + (thisBeaconId == null ? 43 : thisBeaconId.hashCode());
        final long thisRssi = Double.doubleToLongBits(this.getRssi());
        result = result * prime + (int) (thisRssi >>> 32 ^ thisRssi);
        final Object thisTimestamp = this.getTimestamp();
        result = result * prime + (thisTimestamp == null ? 43 : thisTimestamp.hashCode());
        return result;
    }

    public String toString() {
        return "Observation(raspId=" + this.getRaspId() + ", beaconId=" + this.getBeaconId() + 
                ", rssi=" + this.getRssi() + ", timestamp=" + this.getTimestamp() + ")";
    }
}
