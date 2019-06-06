package fi.helsinki.btls.domain;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/***
 * Class representing one beacon.
 */
public class Beacon {
    private String id;
    private double minVolume;
    private List<ObservationModel> observations;

    public Beacon(String id) {
        this.id = id;
        this.minVolume = Double.MAX_VALUE;
    }

    public String getId() {
        return id;
    }

    public double getMinVolume() {
        return minVolume;
    }

    public List<ObservationModel> getObservations() {
        return observations;
    }

    public void setObservations(List<ObservationModel> observations) {
        this.observations = observations;
        double minVol = observations
                .stream()
                .map(x -> x.getVolume())
                .min(Comparator.comparing(Double::valueOf))
                .get();
        if (minVol < this.minVolume) {
            this.minVolume = minVol;
        }

    }
}
