package fi.helsinki.btls;

import static org.junit.Assert.*;

import fi.helsinki.btls.services.IObserverService;
import fi.helsinki.btls.services.LocationService2D;
import fi.helsinki.btls.services.LocationService3D;
import fi.helsinki.btls.services.ObserverService;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import fi.helsinki.btls.datamodels.*;

/**
 * Tests for observation mocker.
 */
public class ObservationGeneratorTest {
    private List<String> obsKeys;

    @Before
    public void setUp() {
        obsKeys = new ArrayList<>();
        obsKeys.add("observer-1");
        obsKeys.add("observer-2");
        obsKeys.add("observer-3");
        obsKeys.add("observer-4");
        obsKeys.add("observer-5");
        obsKeys.add("observer-6");
        obsKeys.add("observer-7");
        obsKeys.add("observer-8");
    }

    @Test
    public void generatorCanBeUsed() {
        ObservationGenerator gen = new ObservationGenerator(12, 1, obsKeys);

        List<Beacon> beacons = gen.getBeacons();
        assertEquals(1, beacons.size());
        assertEquals(1, beacons.get(0).getObservations().size());
    }

    @Test
    public void genCanCReateMultipleSametIme() {
        ObservationGenerator gen = new ObservationGenerator(1, 30, obsKeys);

        List<Observation> observations = gen.getBeacons().get(0).getObservations();
        assertEquals(30, observations.size());
    }

    @Test
    public void canCreateMUltipleBeacons() {
        ObservationGenerator gen = new ObservationGenerator(14, 20, obsKeys);

        List<Beacon> beacons = gen.getBeacons();
        assertTrue(3 < beacons.size());
    }

    @Test
    public void observationsDontDeleteBetweenCalls() {
        ObservationGenerator gen = new ObservationGenerator(obsKeys);

        List<Beacon> beacons = gen.getBeacons();
        assertTrue(2 < beacons.size());

        int first = beacons.stream().mapToInt(c -> c.getObservations().size()).sum();

        List<Beacon> moreBeacons = gen.getBeacons();
        assertTrue(4 < moreBeacons.size());

        int second = moreBeacons.stream().mapToInt(c -> c.getObservations().size()).sum();

        assertTrue(first < second);
    }

    @Test
    public void beaconsObservationCantGoOverLimit() {
        ObservationGenerator gen = new ObservationGenerator(1, 6000, obsKeys);

        List<Observation> observations = gen.getBeacons().get(0).getObservations();
        assertEquals(6000, observations.size());

        List<Observation> limit = gen.getBeacons().get(0).getObservations();
        assertEquals(9999, limit.size());
    }

    @Test
    public void a() {
        ObservationGenerator gen = new ObservationGenerator(14, 500, obsKeys);
        IObserverService s = new ObserverService(3);

        s.addObserver(createObserver("observer-1", 0, 0, 0));
        s.addObserver(createObserver("observer-8", 10000, 10000, 10000));
        s.addObserver(createObserver("observer-7", 10000, 10000, 0));
        s.addObserver(createObserver("observer-6", 0, 10000, 10000));
        s.addObserver(createObserver("observer-5", 10000, 0, 10000));
        s.addObserver(createObserver("observer-4", 0, 0, 10000));
        s.addObserver(createObserver("observer-3", 0, 10000, 0));
        s.addObserver(createObserver("observer-2", 10000, 0, 0));

        List<Location> locations = new LocationService3D(s).calculateAllLocations(gen.getBeacons());


        for (Location loc : locations) {
            System.out.println(loc.getX() + ", " + loc.getY() + ", " + ((Location3D) loc).getZ());
        }
    }
    
    private Observer createObserver(String s, double v, double v2, double v3) {
        return new Observer(s, v, v2, v3);
    }
}
