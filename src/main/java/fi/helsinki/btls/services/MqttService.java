package fi.helsinki.btls.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import fi.helsinki.btls.datamodels.Beacon;
import fi.helsinki.btls.datamodels.Location;
import fi.helsinki.btls.datamodels.Observation;
import fi.helsinki.btls.io.IMqttProvider;
import fi.helsinki.btls.io.UbiMqttProvider;

/**
 * MqttService.
 */
public class MqttService implements IMqttService{
    public static final int MAX_OBSERVATIONS = 10000;
    private static final int BEACON_LIFETIME = 5;
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

        this.gson = createGson();

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

    private Gson createGson() {
        GsonBuilder gb = new GsonBuilder();
        gb.serializeSpecialFloatingPointValues();
        gb.enableComplexMapKeySerialization();
        gb.serializeNulls();
        gb.setLenient();

        return gb.create();
    }

    private void addObservation(Observation observation) {
        if (!beacons.containsKey(observation.getBeaconId())) {
            beacons.put(observation.getBeaconId(), new Beacon(observation.getBeaconId(), BEACON_LIFETIME));
        }


        Beacon beacon = beacons.get(observation.getBeaconId());
        List<Observation> observations = beacon.getObservations();

        if (observations.size() >= MAX_OBSERVATIONS) {
            observations.remove(0);
        }

        observation.setTimestamp(LocalDateTime.now());
        observations.add(observation);
        beacon.setObservations(observations);
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
