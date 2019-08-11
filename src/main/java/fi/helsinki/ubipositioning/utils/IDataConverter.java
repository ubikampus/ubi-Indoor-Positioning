package fi.helsinki.ubipositioning.utils;

/**
 * Interface to generalize rssi conversation into more useful format
 * so that trilateration is possible and more effective.
 */
public interface IDataConverter {
    double convert(double rssi, double measuredPower);
}
