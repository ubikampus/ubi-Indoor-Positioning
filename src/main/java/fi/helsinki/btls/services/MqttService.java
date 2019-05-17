package fi.helsinki.btls.services;

import fi.helsinki.btls.domain.ObservationModel;
import fi.helsinki.btls.io.IMqttProvider;

import java.util.List;

public class MqttService {
    private static final int MAX_OBSERVATIONS = 10000;
    private List<ObservationModel> observations;
    private IMqttProvider provider;

    public MqttService(IMqttProvider provider) {
        this.provider = provider;
    }
}
