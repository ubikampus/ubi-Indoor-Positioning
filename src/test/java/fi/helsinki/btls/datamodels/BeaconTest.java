package fi.helsinki.btls.datamodels;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for beacon.
 */
public class BeaconTest {

    @Test
    public void getObservationsReturnsOnlyRecentObservations() {
        Beacon b = new Beacon("1", 10, null);
        List<Observation> obs = new ArrayList();
        obs.add(new Observation("raspi", "beacon", 50, LocalDateTime.now()));
        obs.add(new Observation("raspi", "beacon", 50, LocalDateTime.now().minusMinutes(1)));
        b.setObservations(obs);
        assertTrue(b.getObservations().size() == 1);
    }
}
