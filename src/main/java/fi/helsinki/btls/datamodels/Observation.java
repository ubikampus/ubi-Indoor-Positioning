package fi.helsinki.btls.datamodels;

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

    public Observation(String raspId, String beaconId, double rssi) {
        this.raspId = raspId;
        this.beaconId = beaconId;
        this.rssi = rssi;
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

    public void setRaspId(String raspId) {
        this.raspId = raspId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public void setRssi(double rssi) {
        this.rssi = rssi;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Observation)) return false;
        final Observation other = (Observation) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$raspId = this.getRaspId();
        final Object other$raspId = other.getRaspId();
        if (this$raspId == null ? other$raspId != null : !this$raspId.equals(other$raspId)) return false;
        final Object this$beaconId = this.getBeaconId();
        final Object other$beaconId = other.getBeaconId();
        if (this$beaconId == null ? other$beaconId != null : !this$beaconId.equals(other$beaconId)) return false;
        if (Double.compare(this.getRssi(), other.getRssi()) != 0) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Observation;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $raspId = this.getRaspId();
        result = result * PRIME + ($raspId == null ? 43 : $raspId.hashCode());
        final Object $beaconId = this.getBeaconId();
        result = result * PRIME + ($beaconId == null ? 43 : $beaconId.hashCode());
        final long $rssi = Double.doubleToLongBits(this.getRssi());
        result = result * PRIME + (int) ($rssi >>> 32 ^ $rssi);
        return result;
    }

    public String toString() {
        return "Observation(raspId=" + this.getRaspId() + ", beaconId=" + this.getBeaconId() + ", rssi=" + this.getRssi() + ")";
    }
}
