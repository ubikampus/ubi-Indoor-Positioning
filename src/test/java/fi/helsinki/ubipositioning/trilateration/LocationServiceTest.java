package fi.helsinki.ubipositioning.trilateration;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fi.helsinki.ubipositioning.datamodels.*;
import fi.helsinki.ubipositioning.utils.*;

/**
 * Tests for locations trilateration calculations.
 */
public class LocationServiceTest {
    private IObserverService observers;
    private Map<String, Cord> rasps;
    private IResultConverter resultConverter;

    // value in the rssi field is already euclidean distance so no actions needed.
    private ISignalMapper signalMapper = (rssi, measuredPower) -> rssi;
    
    // Lambda expression that tells to return first value in the array.
    private IMeasurementFilter filter = measurements -> measurements[0];

    private void setUp2D() {
        resultConverter = new IResultConverter() {
            @Override
            public Location convert(Beacon beacon, double[] centroid, double[] standardDeviation, RealMatrix covMatrix) {
                EigenDecomposition ed = new EigenDecomposition(covMatrix);
                double[] realEigenvalues = ed.getRealEigenvalues();
                RealVector principal = ed.getEigenvector(0);
                double highestEigenValue = realEigenvalues[0];

                for (int i = 1; i < realEigenvalues.length; i++) {
                    RealVector eigenvector = ed.getEigenvector(i);
                    double eigenvalue = realEigenvalues[i];

                    if (highestEigenValue < eigenvalue) {
                        principal = eigenvector;
                        highestEigenValue = eigenvalue;
                    }
                }

                double[] asArray = principal.toArray();
                double arctan = Math.atan(asArray[0] / asArray[1]); // angle is gotten through arc tangent.

                return new Location2D(beacon.getId(),
                        centroid[0], centroid[1],
                        standardDeviation[0], standardDeviation[1], arctan);
            }
        };

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
        resultConverter = new IResultConverter() {
            @Override
            public Location convert(Beacon beacon, double[] centroid, double[] standardDeviation, RealMatrix covMatrix) {
                return new Location3D(beacon.getId(),
                        centroid[0], centroid[1], centroid[2],
                        standardDeviation[0], standardDeviation[1], standardDeviation[2]);
            }
        };

        observers = new ObserverService(3);
        rasps = new HashMap<>();

        createObserver(0, 0, 0, "rasp-1");

        createObserver(100000, 0, 0, "rasp-2");
        createObserver(0, 100000, 0, "rasp-3");
        createObserver(0, 0, 100000, "rasp-4");
        createObserver(100000, 100000, 0, "rasp-5");
        createObserver(0, 100000, 100000, "rasp-6");
        createObserver(100000, 0, 100000, "rasp-7");
        createObserver(100000, 100000, 100000, "rasp-8");

        createObserver(50000, 0, 0, "rasp-9");
        createObserver(0, 50000, 0, "rasp-10");
        createObserver(0, 0, 50000, "rasp-11");
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
        ILocationService locationService = new LocationService(observers, signalMapper, resultConverter, filter);

        List<Observation> obs = new ArrayList<>();
        Beacon beacon = new Beacon("beacon-1");
        Cord wanted = new Cord(2000, 5000);

        obs.add(createObservation2D("rasp-1", beacon.getId(), wanted));
        obs.add(createObservation2D("rasp-2", beacon.getId(), wanted));
        obs.add(createObservation2D("rasp-3", beacon.getId(), wanted));
        obs.add(createObservation2D("rasp-4", beacon.getId(), wanted));

        beacon.setObservations(obs);
        beacon.setMeasuredPower(0);

        Location location = locationService.calculateLocation(beacon);

        assertNotNull(location);
        assertEquals(wanted.x, location.getX(), 0.05);
        assertEquals(wanted.y, location.getY(), 0.05);
        assertTrue(location.getXr() < 0.5);
        assertTrue(location.getYr() < 0.5);
    }

    @Test
    public void simpleLocationCalculation3D() {
        setUp3D();
        ILocationService locationService = new LocationService(observers, signalMapper, resultConverter, filter);

        List<Observation> obs = new ArrayList<>();
        Beacon beacon = new Beacon("beacon-1");
        Cord wanted = new Cord(2000, 5000, 3500);

        obs.add(createObservation3D("rasp-1", beacon.getId(), wanted));
        obs.add(createObservation3D("rasp-2", beacon.getId(), wanted));
        obs.add(createObservation3D("rasp-3", beacon.getId(), wanted));
        obs.add(createObservation3D("rasp-4", beacon.getId(), wanted));
        obs.add(createObservation3D("rasp-5", beacon.getId(), wanted));
        obs.add(createObservation3D("rasp-6", beacon.getId(), wanted));
        obs.add(createObservation3D("rasp-7", beacon.getId(), wanted));
        obs.add(createObservation3D("rasp-8", beacon.getId(), wanted));

        beacon.setObservations(obs);
        beacon.setMeasuredPower(0);

        Location3D location = (Location3D) locationService.calculateLocation(beacon);

        assertNotNull(location);
        assertEquals(wanted.x, location.getX(), 0.05);
        assertEquals(wanted.y, location.getY(), 0.05);
        assertEquals(wanted.z, location.getZ(), 0.05);
        assertTrue(location.getXr() < 0.5);
        assertTrue(location.getYr() < 0.5);
        assertTrue(location.getZr() < 0.5);
    }

    @Test
    public void onlyCaresAboutLatest2D() {
        setUp2D();
        ILocationService locationService = new LocationService(observers, signalMapper, resultConverter, filter);

        List<Observation> obs = new ArrayList<>();
        Beacon beacon = new Beacon("beacon-1");
        Cord first = new Cord(2000, 5000);
        Cord wanted = new Cord(18795, 76533);

        obs.add(createObservation2D("rasp-1", beacon.getId(), first));
        obs.add(createObservation2D("rasp-2", beacon.getId(), first));
        obs.add(createObservation2D("rasp-3", beacon.getId(), first));
        obs.add(createObservation2D("rasp-4", beacon.getId(), first));

        obs.add(createObservation2D("rasp-1", beacon.getId(), wanted));
        obs.add(createObservation2D("rasp-2", beacon.getId(), wanted));
        obs.add(createObservation2D("rasp-3", beacon.getId(), wanted));
        obs.add(createObservation2D("rasp-4", beacon.getId(), wanted));

        beacon.setObservations(obs);
        beacon.setMeasuredPower(0);
        Location location = locationService.calculateLocation(beacon);

        assertNotNull(location);
        assertEquals(wanted.x, location.getX(), 0.05);
        assertEquals(wanted.y, location.getY(), 0.05);
        assertTrue(location.getXr() < 0.5);
        assertTrue(location.getYr() < 0.5);
    }

    @Test
    public void onlyCaresAboutLatest3D() {
        setUp3D();
        ILocationService locationService = new LocationService(observers, signalMapper, resultConverter, filter);

        List<Observation> obs = new ArrayList<>();
        Beacon beacon = new Beacon("beacon-1");
        Cord first = new Cord(2000, 5000, 3500);
        Cord wanted = new Cord(100, 66800, 30002);

        obs.add(createObservation3D("rasp-1", beacon.getId(), first));
        obs.add(createObservation3D("rasp-2", beacon.getId(), first));
        obs.add(createObservation3D("rasp-3", beacon.getId(), first));
        obs.add(createObservation3D("rasp-4", beacon.getId(), first));
        obs.add(createObservation3D("rasp-5", beacon.getId(), first));
        obs.add(createObservation3D("rasp-6", beacon.getId(), first));
        obs.add(createObservation3D("rasp-7", beacon.getId(), first));
        obs.add(createObservation3D("rasp-8", beacon.getId(), first));

        obs.add(createObservation3D("rasp-1", beacon.getId(), wanted));
        obs.add(createObservation3D("rasp-2", beacon.getId(), wanted));
        obs.add(createObservation3D("rasp-3", beacon.getId(), wanted));
        obs.add(createObservation3D("rasp-4", beacon.getId(), wanted));
        obs.add(createObservation3D("rasp-5", beacon.getId(), wanted));
        obs.add(createObservation3D("rasp-6", beacon.getId(), wanted));
        obs.add(createObservation3D("rasp-7", beacon.getId(), wanted));
        obs.add(createObservation3D("rasp-8", beacon.getId(), wanted));

        beacon.setObservations(obs);
        beacon.setMeasuredPower(0);
        Location3D location = (Location3D) locationService.calculateLocation(beacon);

        assertNotNull(location);
        assertEquals(wanted.x, location.getX(), 0.05);
        assertEquals(wanted.y, location.getY(), 0.05);
        assertEquals(wanted.z, location.getZ(), 0.05);
        assertTrue(location.getXr() < 0.5);
        assertTrue(location.getYr() < 0.5);
        assertTrue(location.getZr() < 0.5);
    }

    @Test
    public void locationCannotBeCalculatedWithoutObs2D() {
        setUp2D();
        ILocationService locationService = new LocationService(observers, signalMapper, resultConverter, filter);

        List<Observation> obs = new ArrayList<>();
        Beacon beacon = new Beacon("beacon-1");
        beacon.setMeasuredPower(0);
        beacon.setObservations(obs);

        try {
            Location location = locationService.calculateLocation(beacon);
            fail("Exception wasn't thrown!");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void locationCannotBeCalculatedWithoutObs3D() {
        setUp3D();
        ILocationService locationService = new LocationService(observers, signalMapper, resultConverter, filter);

        List<Observation> obs = new ArrayList<>();
        Beacon beacon = new Beacon("beacon-1");
        beacon.setMeasuredPower(0);
        beacon.setObservations(obs);

        try {
            Location location = locationService.calculateLocation(beacon);
            fail("Exception wasn't thrown!");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void calculateAllLocations2D() {
        setUp2D();
        ILocationService locationService = new LocationService(observers, signalMapper, resultConverter, filter);
        List<Cord> wanted = new ArrayList<>();
        List<Beacon> beacons = new ArrayList<>();

        // Beacons.
        List<Observation> obs = new ArrayList<>();
        Beacon beacon = new Beacon("beacon-1");
        Cord cord = new Cord(1657, 94519);
        wanted.add(cord);

        obs.add(createObservation2D("rasp-1", beacon.getId(), cord));
        obs.add(createObservation2D("rasp-2", beacon.getId(), cord));
        obs.add(createObservation2D("rasp-3", beacon.getId(), cord));
        obs.add(createObservation2D("rasp-4", beacon.getId(), cord));

        beacon.setObservations(obs);
        beacon.setMeasuredPower(0);
        beacons.add(beacon);

        obs = new ArrayList<>();
        beacon = new Beacon("beacon-2");
        cord = new Cord(55767, 20000);
        wanted.add(cord);

        obs.add(createObservation2D("rasp-1", beacon.getId(), cord));
        obs.add(createObservation2D("rasp-2", beacon.getId(), cord));
        obs.add(createObservation2D("rasp-3", beacon.getId(), cord));
        obs.add(createObservation2D("rasp-4", beacon.getId(), cord));

        beacon.setObservations(obs);
        beacon.setMeasuredPower(0);
        beacons.add(beacon);

        obs = new ArrayList<>();
        beacon = new Beacon("beacon-3");
        cord = new Cord(101, 99878);
        wanted.add(cord);

        obs.add(createObservation2D("rasp-1", beacon.getId(), cord));
        obs.add(createObservation2D("rasp-2", beacon.getId(), cord));
        obs.add(createObservation2D("rasp-3", beacon.getId(), cord));
        obs.add(createObservation2D("rasp-4", beacon.getId(), cord));

        beacon.setObservations(obs);
        beacon.setMeasuredPower(0);
        beacons.add(beacon);

        obs = new ArrayList<>();
        beacon = new Beacon("beacon-4");
        cord = new Cord(54791, 75682);
        wanted.add(cord);

        obs.add(createObservation2D("rasp-1", beacon.getId(), cord));
        obs.add(createObservation2D("rasp-2", beacon.getId(), cord));
        obs.add(createObservation2D("rasp-3", beacon.getId(), cord));
        obs.add(createObservation2D("rasp-4", beacon.getId(), cord));

        beacon.setObservations(obs);
        beacon.setMeasuredPower(0);
        beacons.add(beacon);

        obs = new ArrayList<>();
        beacon = new Beacon("beacon-5");
        cord = new Cord(4300, 6888);
        wanted.add(cord);

        obs.add(createObservation2D("rasp-1", beacon.getId(), cord));
        obs.add(createObservation2D("rasp-2", beacon.getId(), cord));
        obs.add(createObservation2D("rasp-3", beacon.getId(), cord));
        obs.add(createObservation2D("rasp-4", beacon.getId(), cord));

        beacon.setObservations(obs);
        beacon.setMeasuredPower(0);
        beacons.add(beacon);

        // Assertion.

        for (int i = 0; i < beacons.size(); i++) {
            Cord real = wanted.get(i);
            Location loc = locationService.calculateLocation(beacons.get(i));

            assertNotNull(loc);
            assertEquals(real.x, loc.getX(), 0.05);
            assertEquals(real.y, loc.getY(), 0.05);
            assertTrue(loc.getXr() < 0.5);
            assertTrue(loc.getYr() < 0.5);
        }
    }

    @Test
    public void calculateAllLocations3D() {
        setUp3D();
        ILocationService locationService = new LocationService(observers, signalMapper, resultConverter, filter);
        List<Cord> wanted = new ArrayList<>();
        List<Beacon> beacons = new ArrayList<>();

        // Beacons.
        List<Observation> obs = new ArrayList<>();
        Beacon beacon = new Beacon("beacon-1");
        Cord cord = new Cord(2000, 5000, 3500);
        wanted.add(cord);

        obs.add(createObservation3D("rasp-1", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-2", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-3", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-4", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-5", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-6", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-7", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-8", beacon.getId(), cord));

        beacon.setObservations(obs);
        beacon.setMeasuredPower(0);
        beacons.add(beacon);

        obs = new ArrayList<>();
        beacon = new Beacon("beacon-2");
        cord = new Cord(29000, 14567, 31);
        wanted.add(cord);

        obs.add(createObservation3D("rasp-1", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-2", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-3", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-4", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-5", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-6", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-7", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-8", beacon.getId(), cord));

        beacon.setObservations(obs);
        beacon.setMeasuredPower(0);
        beacons.add(beacon);

        obs = new ArrayList<>();
        beacon = new Beacon("beacon-3");
        cord = new Cord(93126, 14544, 18999);
        wanted.add(cord);

        obs.add(createObservation3D("rasp-1", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-2", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-3", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-4", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-5", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-6", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-7", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-8", beacon.getId(), cord));

        beacon.setObservations(obs);
        beacon.setMeasuredPower(0);
        beacons.add(beacon);

        obs = new ArrayList<>();
        beacon = new Beacon("beacon-4");
        cord = new Cord(1200, 56222, 7843);
        wanted.add(cord);

        obs.add(createObservation3D("rasp-1", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-2", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-3", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-4", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-5", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-6", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-7", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-8", beacon.getId(), cord));

        beacon.setObservations(obs);
        beacon.setMeasuredPower(0);
        beacons.add(beacon);

        obs = new ArrayList<>();
        beacon = new Beacon("beacon-5");
        cord = new Cord(4300, 6888, 50030);
        wanted.add(cord);

        obs.add(createObservation3D("rasp-1", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-2", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-3", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-4", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-5", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-6", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-7", beacon.getId(), cord));
        obs.add(createObservation3D("rasp-8", beacon.getId(), cord));

        beacon.setObservations(obs);
        beacon.setMeasuredPower(0);
        beacons.add(beacon);

        for (int i = 0; i < beacons.size(); i++) {
            Cord real = wanted.get(i);
            Location loc = locationService.calculateLocation(beacons.get(i));

            assertNotNull(loc);
            assertEquals(Location3D.class, loc.getClass());
            Location3D ddd = (Location3D) loc;

            assertEquals(real.x, ddd.getX(), 0.05);
            assertEquals(real.y, ddd.getY(), 0.05);
            assertEquals(real.z, ddd.getZ(), 0.05);
            assertTrue(ddd.getXr() < 0.5);
            assertTrue(ddd.getYr() < 0.5);
            assertTrue(ddd.getZr() < 0.5);
        }
    }

    private Observation createObservation2D(String raspId, String beaconId, Cord wanted) {
        return new Observation(raspId, beaconId, euclideanDistance2D(wanted, rasps.get(raspId)));
    }

    private Observation createObservation3D(String raspId, String beaconId, Cord wanted) {
        return new Observation(raspId, beaconId, euclideanDistance3D(wanted, rasps.get(raspId)));
    }

    private double euclideanDistance2D(Cord c1, Cord c2) {
        return Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2));
    }

    private double euclideanDistance3D(Cord c1, Cord c2) {
        return Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2) + Math.pow(c1.z - c2.z, 2));
    }

    private static class Cord {
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
