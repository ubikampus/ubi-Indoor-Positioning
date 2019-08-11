package fi.helsinki.ubipositioning.utils;

/**
 * Interface for smoothing inaccuracy out of raw data.
 */
public interface IMeasurementFilter {
    double smooth(Double[] measurements);
}
