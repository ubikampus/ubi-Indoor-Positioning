package fi.helsinki.ubipositioning.utils;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test class for PathLossModel.
 */
public class PathLossModelTest {
    @Test
    public void getDistanceFromRssiWorks() {
        PathLossModel pathLossModel = new PathLossModel();
        pathLossModel.setCalculateDistance(true);
        assertEquals(pathLossModel.calculateDistance(-80, -69), 3548.1, 0.10);
    }

}
