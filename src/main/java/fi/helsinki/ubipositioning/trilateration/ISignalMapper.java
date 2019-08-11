package fi.helsinki.ubipositioning.trilateration;

/**
 * Interface to generalize rssi conversation into more useful format
 * so that trilateration is possible and more effective.
 */
public interface ISignalMapper {
    double convert(double rssi, double measuredPower);
}
