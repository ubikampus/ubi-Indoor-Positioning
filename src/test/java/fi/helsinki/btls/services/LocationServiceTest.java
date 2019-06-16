package fi.helsinki.btls.services;

import static org.junit.Assert.*;

import fi.helsinki.btls.domain.Observer;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fi.helsinki.btls.domain.Beacon;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;

/**
 * Test class for location services.
 */
public class LocationServiceTest {
    private IObserverService observers;
    private Map<String, Cord> rasps;

    private void setUp2D() {
        observers = new ObserverService(2);
        rasps = new HashMap<>();

        createObserver(0, 0, "rasp-1");

        createObserver(100000, 0, "rasp-2");
        createObserver(0, 100000, "rasp-3");
        createObserver(100000, 100000, "rasp-4");

        createObserver(50000, 0, "rasp-5");
        createObserver(0, 50000, "rasp-6");
        createObserver(50000, 50000, "rasp-7");
    }

    private void createObserver(double x, double y, String id) {
        Cord c = new Cord(x, y);
        rasps.put(id, c);
        observers.addObserver(new Observer(id, c.x, c.y));
    }


    private void setUp3D() {
        observers = new ObserverService(3);
        rasps = new HashMap<>();

        createObserver(0, 0, 0, "rasp-1");

        createObserver(100000, 0, 0, "rasp-2");
        createObserver(0, 100000, 0, "rasp-3");
        createObserver(0, 0,100000, "rasp-4");
        createObserver(100000, 100000, 0, "rasp-5");
        createObserver(0, 100000, 100000, "rasp-6");
        createObserver(100000, 0, 100000, "rasp-7");
        createObserver(100000, 100000, 100000, "rasp-8");

        createObserver(50000, 0, 0, "rasp-9");
        createObserver(0, 50000, 0, "rasp-10");
        createObserver(0, 0,50000, "rasp-11");
        createObserver(50000, 50000, 0, "rasp-12");
        createObserver(0, 50000, 50000, "rasp-13");
        createObserver(50000, 0, 50000, "rasp-14");
        createObserver(50000, 50000, 50000, "rasp-15");
    }

    private void createObserver(double x, double y, double z, String id) {
        Cord c = new Cord(x, y, z);
        rasps.put(id, c);
        observers.addObserver(new Observer(id, c.x, c.y, c.z));
    }

    @Test
    public void simpleLocationCalculation2D() {
        setUp2D();
        ILocationService locationService = new LocationService2D(observers);

        List<ObservationModel> obs = new ArrayList<>();
        Beacon beacon = new Beacon("beacon-1");
        beacon.setMinRSSI(0);
        Cord wanted = new Cord(2000, 5000);

        obs.add(new ObservationModel("rasp-1", beacon.getId(), euclideanDistance2D(wanted, rasps.get("rasp-1"))));
        obs.add(new ObservationModel("rasp-2", beacon.getId(), euclideanDistance2D(wanted, rasps.get("rasp-2"))));
        obs.add(new ObservationModel("rasp-3", beacon.getId(), euclideanDistance2D(wanted, rasps.get("rasp-3"))));
        obs.add(new ObservationModel("rasp-4", beacon.getId(), euclideanDistance2D(wanted, rasps.get("rasp-4"))));

        beacon.setObservations(obs);
        LocationModel locationModel = locationService.calculateLocation(beacon);

        assertNotNull(locationModel);
        System.out.println(locationModel);
    }

    @Test
    public void simpleLocationCalculation3D() {
        setUp3D();
        ILocationService locationService = new LocationService3D(observers);

        List<ObservationModel> obs = new ArrayList<>();
        Beacon beacon = new Beacon("beacon-1");
        beacon.setMinRSSI(0);
        Cord wanted = new Cord(2000, 5000, 3500);

        obs.add(new ObservationModel("rasp-1", beacon.getId(), euclideanDistance3D(wanted, rasps.get("rasp-1"))));
        obs.add(new ObservationModel("rasp-2", beacon.getId(), euclideanDistance3D(wanted, rasps.get("rasp-2"))));
        obs.add(new ObservationModel("rasp-3", beacon.getId(), euclideanDistance3D(wanted, rasps.get("rasp-3"))));
        obs.add(new ObservationModel("rasp-4", beacon.getId(), euclideanDistance3D(wanted, rasps.get("rasp-4"))));
        obs.add(new ObservationModel("rasp-5", beacon.getId(), euclideanDistance3D(wanted, rasps.get("rasp-5"))));
        obs.add(new ObservationModel("rasp-6", beacon.getId(), euclideanDistance3D(wanted, rasps.get("rasp-6"))));
        obs.add(new ObservationModel("rasp-7", beacon.getId(), euclideanDistance3D(wanted, rasps.get("rasp-7"))));
        obs.add(new ObservationModel("rasp-8", beacon.getId(), euclideanDistance3D(wanted, rasps.get("rasp-8"))));

        beacon.setObservations(obs);
        LocationModel locationModel = locationService.calculateLocation(beacon);

        assertNotNull(locationModel);
        System.out.println(locationModel);
    }

    private double euclideanDistance2D(Cord c1, Cord c2) {
        return Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2));
    }

    private double euclideanDistance3D(Cord c1, Cord c2) {
        return Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2) + Math.pow(c1.z - c2.z, 2));
    }

    private class Cord {
        private double x;
        private double y;
        private double z;

        Cord(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        Cord(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
