package fi.helsinki.btls;

import static org.junit.Assert.*;
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
}
