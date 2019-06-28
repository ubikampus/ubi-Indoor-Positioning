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
        b = new Beacon("1", -100, null, 1);
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

    @Test
    public void setObservationsUpdatesMinRssiAccordingly() {
        obs.add(new Observation("raspi", "beacon", -50, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", -60, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", -30, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", -90, LocalDateTime.now()));
        b.setObservations(obs);
        assertTrue(b.getMinRSSI() == -30);
    }

    @Test
    public void setObservationsUpdatesMinRssiAccordinglyWhenMinRssiShouldNotBeChanged() {
        obs.add(new Observation("raspi", "beacon", -150, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", -160, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", -130, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", -190, LocalDateTime.now()));
        b.setObservations(obs);
        assertTrue(b.getMinRSSI() == -100);
    }
}
