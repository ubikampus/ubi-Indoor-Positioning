
package fi.helsinki.ubipositioning.mqtt;

import java.util.List;
import fi.helsinki.ubipositioning.datamodels.Beacon;
import fi.helsinki.ubipositioning.datamodels.Location;
import fi.helsinki.ubipositioning.datamodels.Observation;

/**
 * Interface for mqtt service.
 */
public interface IMqttService {
    List<Observation> getObservations();
    List<Beacon> getBeacons();

    void publish(List<Location> locations);
    void publish(Location location);
    void setObservationLifetime(int observationLifetime);
    int getObservationLifetime();
}
