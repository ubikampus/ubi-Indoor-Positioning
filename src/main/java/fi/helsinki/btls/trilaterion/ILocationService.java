package fi.helsinki.btls.trilaterion;

import java.util.*;
import fi.helsinki.btls.datamodels.*;

/**
 * Interface for location calculation service.
 */
public interface ILocationService {
    Location calculateLocation(Beacon beacon);
    List<Location> calculateAllLocations(List<Beacon> beacons);
}
