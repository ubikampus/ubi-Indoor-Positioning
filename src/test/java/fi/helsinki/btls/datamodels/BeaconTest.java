package fi.helsinki.btls.datamodels;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for beacon.
 */
public class BeaconTest {

    Beacon b;
    List<Observation> obs;
    @Before
    public void setUp() {
        b = new Beacon("1", 10, null, 1);
        obs = new ArrayList();
    }

    @Test
    public void getObservationsReturnsOnlyRecentObservations() {
        obs.add(new Observation("raspi", "beacon", 50, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", 50, LocalDateTime.now().minusMinutes(1)));
        b.setObservations(obs);
        assertTrue(b.getObservations().size() == 1);
    }

    @Test
    public void getObservationsReturnsEmptyListIfAllObservationsExpired() throws InterruptedException {
        obs.add(new Observation("raspi", "beacon", 50, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", 50, LocalDateTime.now()));
        b.setObservations(obs);
        Thread.sleep(1000);
        assertTrue(b.getObservations().size() == 0);
    }


}
