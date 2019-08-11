package fi.helsinki.ubipositioning.utils;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test class for PathLossModel.
 */
public class RssiToMetersTest {
    @Test
    public void getDistanceFromRssiWorks() {
        RssiToMilliMeters pathLossModel = new RssiToMilliMeters(2);
        assertEquals(pathLossModel.convert(-80, -69), 3548.1, 0.10);
    }
}
