package fi.helsinki.ubipositioning.utils;

/**
 * Path loss distance calculation.
 */
public class PathLossModel {

    private boolean calculateDistance;
    private double environmentalFactor;

    public PathLossModel() {
        this.calculateDistance = true;
        this.environmentalFactor = 2;
    }

    public void setCalculateDistance(boolean calculateDistance) {
        this.calculateDistance = calculateDistance;
    }

    /**
     * Converts rssi value into distance of millimeters.
     *
     * @param rssi signal strength that BLE listener has picked up..
     * @param measuredPower Base power of BLE listener.
     *
     * @return distance in millimeters of device which rssi is given.
     */
    public double calculateDistance(double rssi, double measuredPower) {
        if (this.calculateDistance) {
            return Math.pow(10, (measuredPower - rssi) / (10 * this.environmentalFactor)) * 1000;
        } else {
            return rssi - measuredPower;
        }
    }

    public double getEnvironmentalFactor() {
        return environmentalFactor;
    }

    public void setEnvironmentalFactor(int environmentalFactor) {
        this.environmentalFactor = environmentalFactor;
    }
}
