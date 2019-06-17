package fi.helsinki.btls.services;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import fi.helsinki.btls.domain.Beacon;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;
import fi.helsinki.btls.io.IMqttProvider;

/**
 * MqttService.
 */
public class MqttService implements IMqttService{
    private static final int MAX_OBSERVATIONS = 10000;
    private IMqttProvider provider;
    private Gson gson;
    private HashMap<String, List<ObservationModel>> observations;

    /**
     * Creates MqttService.
     * @param provider
     */
    public MqttService(IMqttProvider provider, Gson gson) {
        this.observations = new HashMap<>();
        this.provider = provider;
        this.gson = gson;

        if (this.provider.isDebugMode()) {
            addObservation(new ObservationModel("rasp-1", "1", -80));
            addObservation(new ObservationModel("rasp-1", "2", -70));
            addObservation(new ObservationModel("rasp-2", "1", -90));
            addObservation(new ObservationModel("rasp-2", "2", -50));
            addObservation(new ObservationModel("rasp-3", "1", -30));
            addObservation(new ObservationModel("rasp-3", "2", -60));
            addObservation(new ObservationModel("rasp-2", "2", -75));
        }

        this.provider.setListener((topic, mqttMessage, listenerId) -> {
            try {
                ObservationModel obs = gson.fromJson(mqttMessage.toString(), ObservationModel.class);
                addObservation(obs);
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        });

        this.provider.connect();
    }

    private void addObservation(ObservationModel obs) {
        List<ObservationModel> obsers = observations.get(obs.getBeaconId());
        if (obsers == null) {
            obsers = new ArrayList<>();
            observations.put(obs.getBeaconId(), obsers);
        }
        if (obsers.size() > MAX_OBSERVATIONS) {
            obsers.remove(0);
        }
        obsers.add(obs);
    }

    @Override
    public List<ObservationModel> getObservations() {
        return observations
                .values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public void publish(LocationModel locationModel) {
        this.provider.publish(gson.toJson(locationModel));
        System.out.println("Published: " + locationModel);
    }

    @Override
    public List<Beacon> getBeacons() {
        return observations
                .entrySet()
                .stream()
                .map(x -> {
                    Beacon b = new Beacon(x.getKey());
                    b.setObservations(x.getValue());
                    return b;
                })
                .collect(Collectors.toList());
    }
}
