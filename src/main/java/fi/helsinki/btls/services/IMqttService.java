
package fi.helsinki.btls.services;

import java.util.List;

import fi.helsinki.btls.domain.Beacon;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;

/**
 * Interface for mqtt service.
 */
public interface IMqttService {
    public List<ObservationModel> getObservations();

    public void publish(LocationModel locationModel);
    public List<Beacon> getBeacons();
}
