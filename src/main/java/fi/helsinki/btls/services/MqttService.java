package fi.helsinki.btls.services;

import java.util.List;
import fi.helsinki.btls.domain.ObservationModel;
import fi.helsinki.btls.io.IMqttProvider;

/**
 * MqttService.
 */
public class MqttService {
    private static final int MAX_OBSERVATIONS = 10000;
    private List<ObservationModel> observations;
    private IMqttProvider provider;

    /**
     * Creates MqttService.
     * @param provider
     */
    public MqttService(IMqttProvider provider) {
        this.provider = provider;
    }
}
