package fi.helsinki.btls.datamodels;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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
    private List<Observation> observations;
    private int maxLifetime;

    public Beacon(String id) {
        this(id, 30);
    }

    public Beacon(String id, int maxLifetime) {
        this.id = id;
        this.minRSSI = Double.MIN_VALUE;
        this.observations = new ArrayList<>();
        this.maxLifetime = maxLifetime;
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
                    .max(Comparator.comparing(Double::valueOf))
                    .get();

            if (minVol > this.minRSSI) {
                this.minRSSI = minVol;
            }
        }
    }

    public List<Observation> getObservations() {
        observations = observations
                .stream()
                .filter(x -> x.getTimestamp().isAfter(LocalDateTime.now().minusSeconds(maxLifetime)))
                .collect(Collectors.toList());
        return observations;
    }
}
