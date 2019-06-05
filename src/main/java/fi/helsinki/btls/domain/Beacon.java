package fi.helsinki.btls.domain;

import java.util.List;

/***
 * Class representing one beacon.
 */
public class Beacon {
    private String id;
    private int minVolume;
    private List<ObservationModel> observations;

    public Beacon(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(int minVolume) {
        this.minVolume = minVolume;
    }

    public List<ObservationModel> getObservations() {
        return observations;
    }

    public void setObservations(List<ObservationModel> observations) {
        this.observations = observations;
    }
}
