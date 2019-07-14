package fi.helsinki.ubipositioning.mqtt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import fi.helsinki.ubipositioning.datamodels.Beacon;
import fi.helsinki.ubipositioning.datamodels.Location;
import fi.helsinki.ubipositioning.datamodels.Observation;

/**
 * Implementation of interface to provide consumer possibility to
 * get all the messages that have been sent to subscribed topic after start up
 * and to publish data in messages into a another topic.
 *
 * @see IMqttProvider
 */
public class MqttService implements IMqttService {
    /**
     * Default buffer for storing data that comes from subscribed topic.
     */
    private static final int MAX_MESSAGES_TO_STORE = 10000;
    /**
     * Default lifetime for beacons in seconds.
     */
    private static final int BEACON_LIFETIME = 5;

    private IMqttProvider provider;
    private Gson gson;
    private HashMap<String, Beacon> beacons;
    private int observationLifetime;
    private int maxMessages;

    /**
     * Creates instance of the class.
     *
     * @param mqttUrl Mqtt servers URL without '://' start.
     * @param subTopic Topic to subscribe to so that data can be received as messages.
     * @param pubTopic Topic to publish data as messages to.
     * @param beacons Initial Beacon information.
     */
    public MqttService(String mqttUrl, String subTopic, String pubTopic, List<Beacon> beacons) {
        this(new UbiMqttProvider(mqttUrl, subTopic, pubTopic), beacons);
    }
    /**
     * Creates instance of the class.
     *
     * @param mqttUrl Mqtt servers URL.
     * @param subTopic Topic to subscribe to.
     * @param pubTopic Topic to publish messages to.
     */
    public MqttService(String mqttUrl, String subTopic, String pubTopic) {
        this(new UbiMqttProvider(mqttUrl, subTopic, pubTopic));
    }

    /**
     * Creates instance of the class.
     *
     * @param provider Implementation of the interface that supports listening and publishing to MQTT bus topics.
     */
    public MqttService(IMqttProvider provider) {
        this(provider, new ArrayList<Beacon>());
    }

    /**
     * Creates instance of the class.
     *
     * @param provider Implementation of the interface that supports listening and publishing to MQTT bus topics.
     * @param beacons Initial Beacon information.
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
            }
        });

        this.observationLifetime = BEACON_LIFETIME;
        this.maxMessages = MAX_MESSAGES_TO_STORE;
        this.provider.connect();
    }

    /**
     * Creates Gson instance that serializes data that doesn't fit into JSONs specs.
     *
     * @return Gson object.
     */
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
            beacons.put(observation.getBeaconId(), new Beacon(observation.getBeaconId(), observationLifetime));
        }

        Beacon beacon = beacons.get(observation.getBeaconId());
        List<Observation> observations = beacon.getObservations();

        if (observations.size() >= this.maxMessages) {
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

    /**
     * Sends list of location data as string in json array format using Gson to MQTT bus.
     *
     * @param locations list of data points to publish into MQTT bus.
     */
    @Override
    public void publish(List<Location> locations) {
        this.provider.publish(gson.toJson(locations));
    }

    /**
     * Sends single location object as string in json array format to MQTT bus.
     *
     * @param location data to be send
     */
    @Override
    public void publish(Location location) {
        publish(Collections.singletonList(location));
    }

    @Override
    public List<Beacon> getBeacons() {
        return new ArrayList<>(beacons.values());
    }

    @Override
    public void setObservationLifetime(int observationLifetime) {
        this.observationLifetime = observationLifetime;
    }

    @Override
    public int getObservationLifetime() {
        return observationLifetime;
    }

    @Override
    public void setMaxMessages(int maxMessages) {
        this.maxMessages = maxMessages;
    }

    @Override
    public int getMaxMessages() {
        return maxMessages;
    }
}
