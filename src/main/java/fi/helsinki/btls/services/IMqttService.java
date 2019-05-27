
package fi.helsinki.btls.services;

import java.util.List;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;
import fi.helsinki.btls.io.IMqttProvider;

/**
 * Interface for mqtt service.
 */
public interface IMqttService {
    IMqttProvider getProvider();
    
    public List<ObservationModel> getObservations() throws Exception;

    public void publish(LocationModel locationModel);
}
