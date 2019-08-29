package fi.helsinki.ubipositioning.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fi.helsinki.ubipositioning.datamodels.Beacon;
import fi.helsinki.ubipositioning.datamodels.Observation;

/**
 * BLE data generator for mocking data that BLE listeners produce.
 * Generator intends to mimic real data by randomly generating new observation using old ones as base for them.
 */
public class ObservationGenerator {
    private Map<String, Beacon> beacons;
    private int maxBeacons;
    private int newOnesPerCall;
    private List<String> observerKeys;

    /**
     * Initializes the mock generator.
     *
     * @param maxBeacons How many BLE devices at max can there be.
     * @param newOnesPerCall How many new observations generated during every call.
     * @param observerKeys List of BLE listeners names.
     */
    public ObservationGenerator(int maxBeacons, int newOnesPerCall, List<String> observerKeys) {
        this.maxBeacons = maxBeacons;
        this.newOnesPerCall = newOnesPerCall;
        this.observerKeys = observerKeys;
        beacons = new HashMap<>();
    }

    /**
     * Initializes the mock generator giving default values for amount of BLE devices can be.
     *
     * @param newOnesPerCall How many new observations generated during every call.
     * @param observerKeys List of BLE listeners names.
     */
    public ObservationGenerator(int newOnesPerCall, List<String> observerKeys) {
        this(8, newOnesPerCall, observerKeys);
    }

    /**
     * Initializes the mock generator giving default values for
     *  amount of BLE devices can be and for amount of new observations to be generate during call.
     *
     * @param observerKeys List of BLE listeners names.
     */
    public ObservationGenerator(List<String> observerKeys) {
        this(8, 20, observerKeys);
    }

    /**
     * Get all beacons with their data.
     * Also generates more/new data during every call.
     *
     * @return Updated BLE mock data for usage.
     */
    public List<Beacon> getBeacons() {
        for (int i = 0; i < newOnesPerCall; i++) {
            String k = randomBeacon();
            String o = randomObserver();
            Beacon b = beacons.getOrDefault(k, null);
            double rssi;

            if (b == null) {
                b = new Beacon(k);
                b.setMeasuredPower(0);

                rssi = Math.random() * -70;
            } else {
                Observation latest = null;

                for (Observation s : b.getObservations()) {
                    if (s.getObserverId().equals(o)) {
                        latest = s;
                        break;
                    }
                }

                if (latest == null) {
                    rssi = Math.random() * -35;
                } else {
                    double change = Math.random() * 2 - 1;
                    double previous = latest.getRssi();

                    rssi = previous + change;
                }
            }

            Observation newONe = new Observation(o, k, rssi);
            b.getObservations().add(newONe);

            if (b.getObservations().size() >= 10000) {
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
