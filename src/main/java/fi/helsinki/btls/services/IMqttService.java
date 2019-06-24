
package fi.helsinki.btls.services;

import java.util.List;
import fi.helsinki.btls.datamodels.Beacon;
import fi.helsinki.btls.datamodels.Location;
import fi.helsinki.btls.datamodels.Observation;

/**
 * Interface for mqtt service.
 */
public interface IMqttService {
    public List<Observation> getObservations();
    public List<Beacon> getBeacons();

    public void publish(List<Location> locations);
    public void publish(Location location);
}
