package fi.helsinki.btls.datamodels;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/***
 * Class representation of one beacon.
 */
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class Beacon {
    private String id;
    private double minRSSI;
    private List<Observation> observations;

    public Beacon(String id) {
        this.id = id;
        this.minRSSI = Double.MAX_VALUE;
        this.observations = new ArrayList<>();
    }

    public Beacon(String id, double minRSSI, List<Observation> observations) {
        this.id = id;
        this.minRSSI = minRSSI;
        this.observations = observations;
    }

    public Beacon() {
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
            double minVol = observations
                    .stream()
                    .map(Observation::getRssi)
                    .min(Comparator.comparing(Double::valueOf))
                    .get();

            if (minVol < this.minRSSI) {
                this.minRSSI = minVol;
            }
        }
    }

    public String getId() {
        return this.id;
    }

    public double getMinRSSI() {
        return this.minRSSI;
    }

    public List<Observation> getObservations() {
        return this.observations;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMinRSSI(double minRSSI) {
        this.minRSSI = minRSSI;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Beacon)) return false;
        final Beacon other = (Beacon) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        if (Double.compare(this.getMinRSSI(), other.getMinRSSI()) != 0) return false;
        final Object this$observations = this.getObservations();
        final Object other$observations = other.getObservations();
        if (this$observations == null ? other$observations != null : !this$observations.equals(other$observations))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Beacon;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final long $minRSSI = Double.doubleToLongBits(this.getMinRSSI());
        result = result * PRIME + (int) ($minRSSI >>> 32 ^ $minRSSI);
        final Object $observations = this.getObservations();
        result = result * PRIME + ($observations == null ? 43 : $observations.hashCode());
        return result;
    }

    public String toString() {
        return "Beacon(id=" + this.getId() + ", minRSSI=" + this.getMinRSSI() + ", observations=" + this.getObservations() + ")";
    }
}
