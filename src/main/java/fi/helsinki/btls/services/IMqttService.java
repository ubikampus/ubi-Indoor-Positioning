
package fi.helsinki.btls.services;

import fi.helsinki.btls.domain.ObservationModel;
import java.util.List;

public interface IMqttService {
    
    public List<ObservationModel> getObsercations() throws Exception;
    
    public void publish();
}
