package fi.helsinki.ubipositioning.utils;

import org.apache.commons.math3.linear.RealMatrix;
import fi.helsinki.ubipositioning.datamodels.Beacon;
import fi.helsinki.ubipositioning.datamodels.Location;

/**
 * Interface to generalize result conversation into more human readable format.
 */
public interface IResultConverter {
    Location convert(Beacon beacon, double[] centroid, double[] standardDeviation, RealMatrix covMatrix);
}
