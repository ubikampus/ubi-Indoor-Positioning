package fi.helsinki.btls.domain;

/**
 * Model class for observation data.
 */
public class ObservationModel {
    private String raspId;
    private String beaconId;
    private double volume;

    public ObservationModel() {
    }

    public ObservationModel(String raspId, String beaconId, double volume) {
        this.raspId = raspId;
        this.beaconId = beaconId;
        this.volume = volume;
    }

    public String getRaspId() {
        return raspId;
    }

    public void setRaspId(String raspId) {
        this.raspId = raspId;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "ObservationModel{" +
                "raspId='" + raspId + '\'' +
                ", beaconId='" + beaconId + '\'' +
                ", volume=" + volume +
                '}';
    }
}
