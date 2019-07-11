
package fi.helsinki.ubipositioning.mqtt;

import java.util.List;
import fi.helsinki.ubipositioning.datamodels.Beacon;
import fi.helsinki.ubipositioning.datamodels.Location;
import fi.helsinki.ubipositioning.datamodels.Observation;

/**
 * Interface for mqtt service.
 */
public interface IMqttService {
    public List<Observation> getObservations();
    public List<Beacon> getBeacons();

    public void publish(List<Location> locations);
    public void publish(Location location);
}
