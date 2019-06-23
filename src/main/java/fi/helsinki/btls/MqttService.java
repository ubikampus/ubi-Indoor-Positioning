package fi.helsinki.btls;

import com.google.gson.Gson;
import java.util.*;
import java.util.stream.Collectors;
import fi.helsinki.btls.datamodels.Beacon;
import fi.helsinki.btls.datamodels.Location;
import fi.helsinki.btls.datamodels.Observation;

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
                Observation obs = gson.fromJson(mqttMessage.toString(), Observation.class);
                addObservation(obs);
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        });

        this.provider.connect();
    }

    private void addObservation(Observation observation) {
        if (!beacons.containsKey(observation.getBeaconId())) {
            beacons.put(observation.getBeaconId(), new Beacon(observation.getBeaconId()));
        }

        beacons.get(observation.getBeaconId()).getObservations().add(observation);
    }


    @Override
    public List<Observation> getObservations() {
        return beacons
                .values()
                .stream()
                .map(Beacon::getObservations)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public void publish(List<Location> locations) {
        this.provider.publish(gson.toJson(locations));
        System.out.println("Published: " + locations);
    }

    @Override
    public void publish(Location location) {
        publish(Collections.singletonList(location));
    }

    @Override
    public List<Beacon> getBeacons() {
        return new ArrayList<>(beacons
                .values());
    }
}