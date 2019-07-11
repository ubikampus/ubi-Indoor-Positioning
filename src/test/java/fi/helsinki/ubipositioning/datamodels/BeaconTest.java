package fi.helsinki.ubipositioning.datamodels;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for beacon.
 */
public class BeaconTest {
    private Beacon b;
    private List<Observation> obs;

    @Before
    public void setUp() {
        b = new Beacon("1", -100, null, 1);
        obs = new ArrayList<>();
    }

    @Test
    public void getObservationsReturnsOnlyRecentObservations() {
        obs.add(new Observation("raspi", "beacon", 50, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", 50, LocalDateTime.now().minusMinutes(1)));
        b.setObservations(obs);
        assertEquals(1, b.getObservations().size());
    }

    @Test
    public void getObservationsReturnsEmptyListIfAllObservationsExpired() throws InterruptedException {
        obs.add(new Observation("raspi", "beacon", 50, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", 50, LocalDateTime.now()));
        b.setObservations(obs);
        Thread.sleep(1000);
        assertEquals(0, b.getObservations().size());
    }

    @Test
    public void setObservationsUpdatesMinRssiAccordingly() {
        obs.add(new Observation("raspi", "beacon", -50, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", -60, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", -30, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", -90, LocalDateTime.now()));
        b.setObservations(obs);
        assertEquals(b.getMinRSSI(), -30, 0.0);
    }

    @Test
    public void setObservationsUpdatesMinRssiAccordinglyWhenMinRssiShouldNotBeChanged() {
        obs.add(new Observation("raspi", "beacon", -150, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", -160, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", -130, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", -190, LocalDateTime.now()));
        b.setObservations(obs);
        assertEquals(b.getMinRSSI(), -100, 0.0);
    }
}
