
package fi.helsinki.btls.services;

import java.util.List;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;

/**
 * Interface for mqtt service.
 */
public interface IMqttService {
    
    public List<ObservationModel> getObservations() throws Exception;

    public void publish(LocationModel locationModel);
}
