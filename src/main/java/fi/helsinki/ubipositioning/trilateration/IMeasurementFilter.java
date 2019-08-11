package fi.helsinki.ubipositioning.trilateration;

/**
 * Interface for smoothing inaccuracy out of raw data.
 */
public interface IMeasurementFilter {
    double smooth(Double[] measurements);
}
