package fi.helsinki.btls.services;

import com.google.gson.Gson;
import java.util.*;
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
            beacons.forEach(beacon -> this.beacons.put(beacon.getId(), beacon));
        }

        this.gson = new Gson();

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
    public void publish(List<LocationModel> locationModels) {
        this.provider.publish(gson.toJson(locationModels));
        System.out.println("Published: " + locationModels);
    }

    @Override
    public void publish(LocationModel locationModel) {
        publish(Collections.singletonList(locationModel));
    }

    @Override
    public List<Beacon> getBeacons() {
        return new ArrayList<>(beacons
                .values());
    }
}
