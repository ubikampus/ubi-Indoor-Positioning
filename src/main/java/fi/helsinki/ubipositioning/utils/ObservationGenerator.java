package fi.helsinki.ubipositioning.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fi.helsinki.ubipositioning.datamodels.Beacon;
import fi.helsinki.ubipositioning.datamodels.Observation;
import fi.helsinki.ubipositioning.mqtt.MqttService;

/**
 * Mocker class to create realistic observation data.
 */
public class ObservationGenerator {
    private Map<String, Beacon> beacons = new HashMap<>();
    private int maxBeacons;
    private int newOnesPerCall;
    private List<String> observerKeys;

    public ObservationGenerator(int maxBeacons, int newOnesPerCall, List<String> observerKeys) {
        this.maxBeacons = maxBeacons;
        this.newOnesPerCall = newOnesPerCall;
        this.observerKeys = observerKeys;
    }

    public ObservationGenerator(int newOnesPerCall, List<String> observerKeys) {
        this(8, newOnesPerCall, observerKeys);
    }

    public ObservationGenerator(List<String> observerKeys) {
        this(8, 20, observerKeys);
    }

    /**
     * get all beacons with their data.
     * Also creates more/new observations during every call.
     *
     * @return updated observation data about mock beacons.
     */
    public List<Beacon> getBeacons() {
        for (int i = 0; i < newOnesPerCall; i++) {
            String k = randomBeacon();
            String o = randomObserver();
            Beacon b = beacons.getOrDefault(k, null);
            double rssi;

            if (b == null) {
                b = new Beacon(k);
                b.setMinRSSI(0);

                rssi = Math.random() * -150;
            } else {
                Observation latest = null;

                for (Observation s : b.getObservations()) {
                    if (s.getRaspId().equals(o)) {
                        latest = s;
                    }
                }

                if (latest == null) {
                    rssi = Math.random() * -100;
                } else {
                    double change = Math.random() * 2 - 1;
                    double previous = latest.getRssi();

                    rssi = previous + change;
                }
            }

            Observation newONe = new Observation(o, k, rssi);
            b.getObservations().add(newONe);

            if (b.getObservations().size() >= MqttService.MAX_OBSERVATIONS) {
                b.getObservations().remove(0);
            }

            beacons.put(k, b);
        }

        return new ArrayList<>(beacons.values());
    }

    private String randomBeacon() {
        return "beacon-" + (int) Math.round(Math.random() * (maxBeacons - 1));
    }

    private String randomObserver() {
        return observerKeys.get((int) Math.round(Math.random() * (observerKeys.size() - 1)));
    }
}
