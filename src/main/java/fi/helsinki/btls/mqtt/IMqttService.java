
package fi.helsinki.btls.mqtt;

import java.util.List;
import fi.helsinki.btls.datamodels.Beacon;
import fi.helsinki.btls.datamodels.Location;
import fi.helsinki.btls.datamodels.Observation;

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
