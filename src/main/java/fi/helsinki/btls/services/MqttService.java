package fi.helsinki.btls.services;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;
import fi.helsinki.btls.io.IMqttProvider;

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
        this.observations = new ArrayList<ObservationModel>();
        this.provider = provider;
        this.gson = gson;

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
    public List<ObservationModel> getObservations() {
        /*List<ObservationModel> test = new ArrayList<ObservationModel>();
        test.add(new ObservationModel("rasp1", "", 123.45));
        test.add(new ObservationModel("rasp2", "", 71.3));
        test.add(new ObservationModel("rasp3", "", 18.6));
        test.add(new ObservationModel("rasp4", "", 96.33));
        test.add(new ObservationModel("rasp2", "", 50.33));

        return test;*/
        return observations;
    }

    @Override
    public void publish(LocationModel locationModel) {
        this.provider.publish(locationModel.toString());
        System.out.println("Published: " + locationModel);
    }
}
