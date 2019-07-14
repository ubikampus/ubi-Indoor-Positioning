package fi.helsinki.ubipositioning.trilateration;

import java.util.*;
import fi.helsinki.ubipositioning.datamodels.*;

/**
 * Interface for trilateration calculation in n-dimensional space.
 */
public interface ILocationService {
    Location calculateLocation(Beacon beacon);
    List<Location> calculateAllLocations(List<Beacon> beacons);
    double getDistanceFromRssi(double rssi, double minRssi);
    void setCalculateDistance(boolean calculateDistance);
}
