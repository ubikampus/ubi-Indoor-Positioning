package fi.helsinki.ubipositioning.trilateration;

/**
 * Converter to change rssi into distance at scale of millimeters.
 */
public class RssiToMilliMeters implements ISignalMapper {
    private double environmentalFactor;

    public RssiToMilliMeters(double environmentalFactor) {
        this.environmentalFactor = environmentalFactor;
    }

    /**
     * Converts rssi to distance which is in scale of millimeters.
     *
     * @param rssi signal strength to be converted.
     * @param measuredPower base power signal strength of the device which rssi is converted.
     *
     * @return Distance in millimeters.
     */
    @Override
    public double convert(double rssi, double measuredPower) {
        return Math.pow(10, (measuredPower - rssi) / (10 * this.environmentalFactor)) * 1000;
    }
}
