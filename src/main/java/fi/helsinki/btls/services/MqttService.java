package fi.helsinki.btls.services;

import java.util.List;

import com.google.gson.Gson;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;
import fi.helsinki.btls.io.IMqttProvider;
import fi.helsinki.ubimqtt.IUbiMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * MqttService.
 */
public class MqttService implements IMqttService{
    private static final int MAX_OBSERVATIONS = 10000;
    private List<ObservationModel> observations;
    private IMqttProvider provider;
    private Gson gson;

    /**
     * Creates MqttService.
     * @param provider
     */
    public MqttService(IMqttProvider provider, Gson gson) {

        this.gson = gson;
        this.provider = provider;
        this.provider.setListener(new IUbiMessageListener() {
            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage, String listenerId) throws Exception {
                observations.add(gson.fromJson(mqttMessage.toString(), ObservationModel.class));
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
