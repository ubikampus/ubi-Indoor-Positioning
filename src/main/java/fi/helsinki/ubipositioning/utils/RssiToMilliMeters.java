package fi.helsinki.ubipositioning.utils;

/**
 * Converter to change rssi into distance at scale of millimeters.
 */
public class RssiToMilliMeters implements IDataConverter {
    private double environmentalFactor;

    public RssiToMilliMeters(double environmentalFactor) {
        this.environmentalFactor = environmentalFactor;
    }

    @Override
    public double convert(double rssi, double measuredPower) {
        return Math.pow(10, (measuredPower - rssi) / (10 * this.environmentalFactor)) * 1000;
    }
}
