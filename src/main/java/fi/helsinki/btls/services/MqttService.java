package fi.helsinki.btls.services;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;
import fi.helsinki.btls.io.IMqttProvider;
import fi.helsinki.ubimqtt.IUbiMessageListener;

/**
 * MqttService.
 */
public class MqttService implements IMqttService{
    private static final int MAX_OBSERVATIONS = 10000;
    private List<ObservationModel> observations;
    private IMqttProvider provider;

    /**
     * Creates MqttService.
     * @param provider
     */
    public MqttService(IMqttProvider provider, Gson gson) {
        this.observations = new ArrayList<ObservationModel>();
        this.provider = provider;
        this.provider.setListener((topic, mqttMessage, listenerId) -> {
            try {
                observations.add(gson.fromJson(mqttMessage.toString(), ObservationModel.class));
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        });
        this.provider.connect();

    }

    @Override
    public List<ObservationModel> getObservations() throws Exception {
        return observations;
    }

    @Override
    public void publish(LocationModel locationModel) {
        this.provider.publish(locationModel.toString());
        System.out.println("Published: " + locationModel);
    }
}
