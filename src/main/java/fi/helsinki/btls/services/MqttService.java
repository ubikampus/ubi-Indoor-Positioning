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
import fi.helsinki.btls.io.UbiMqttProvider;

/**
 * MqttService.
 */
public class MqttService implements IMqttService{
    private static final int MAX_OBSERVATIONS = 10000;
    private IMqttProvider provider;
    private Gson gson;
    private HashMap<String, Beacon> beacons;



    /**
     * Creates MqttService.
     * @param mqttUrl Mqtt server URL.
     * @param subTopic Topic to subscribe.
     * @param pubTopic Topic to publish.
     * @param beacons initial Beacon information.
     */
    public MqttService(String mqttUrl, String subTopic, String pubTopic, List<Beacon> beacons) {
        this(new UbiMqttProvider(mqttUrl, subTopic, pubTopic), beacons);
    }
    /**
     * Creates MqttService.
     * @param mqttUrl Mqtt server URL.
     * @param subTopic Topic to subscribe.
     * @param pubTopic Topic to publish.
     */
    public MqttService(String mqttUrl, String subTopic, String pubTopic) {
        this(new UbiMqttProvider(mqttUrl, subTopic, pubTopic));
    }

    /**
     * Creates MqttService.
     * @param provider MqttProvider to subscribe and publish to mqtt bus.
     */
    public MqttService(IMqttProvider provider) {
        this(provider, new ArrayList<Beacon>());
    }

    /**
     * Creates MqttService.
     * @param provider MqttProvider to subscribe and publish to mqtt bus.
     * @param beacons initial Beacon information.
     */
    public MqttService(IMqttProvider provider, List<Beacon> beacons) {
        this.provider = provider;
        this.beacons = new HashMap<>();
        if (beacons != null) {
            for (Beacon beacon : beacons) {
                this.beacons.put(beacon.getId(), beacon);
            }
        }
        this.gson = new Gson();

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

    private void addObservation(ObservationModel observationModel) {
        if (!beacons.containsKey(observationModel.getBeaconId())) {
            beacons.put(observationModel.getBeaconId(), new Beacon(observationModel.getBeaconId()));
        }
        beacons.get(observationModel.getBeaconId()).getObservations().add(observationModel);
    }


    @Override
    public List<ObservationModel> getObservations() {
        return beacons
                .values()
                .stream()
                .map(Beacon::getObservations)
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
        return new ArrayList<>(beacons
                .values());
    }
}
