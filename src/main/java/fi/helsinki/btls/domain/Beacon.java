package fi.helsinki.btls.domain;

import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * Class representation of one beacon.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Beacon {
    private String id;
    private double minRSSI;
    private List<ObservationModel> observations;

    public Beacon(String id) {
        this.id = id;
        this.minRSSI = Double.MAX_VALUE;
    }

    /**
     * Set observations of the beacon.
     * Also updates the min RSSI to equal the new list of observations if needed.
     *
     * @param observations list of observations.
     */
   public void setObservations(List<ObservationModel> observations) {
        this.observations = observations;
        double minVol = observations
                .stream()
                .map(ObservationModel::getRssi)
                .min(Comparator.comparing(Double::valueOf))
                .get();

        if (minVol < this.minRSSI) {
            this.minRSSI = minVol;
        }
    }
}
