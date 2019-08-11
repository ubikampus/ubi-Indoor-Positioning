package fi.helsinki.ubipositioning.trilateration;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for Kalman filter.
 */
public class MeasurementFilterTest {
    @Test
    public void smooth() {
        IMeasurementFilter filter = new MeasurementFilter();
        List<Double> obs = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            obs.add((Math.random() * -10) - 20);
        }

        double smooth = filter.smooth(obs.toArray(new Double[0]));
        assertEquals(-25, smooth, 1.5);
    }

    @Test
    public void smoothMultiple() {
        IMeasurementFilter filter = new MeasurementFilter();
        List<Double> obs = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            obs.add((Math.random() * -10) - 20);
        }

        double smooth = filter.smooth(obs.toArray(new Double[0]));
        assertEquals(-25, smooth, 1.5);

        obs = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            obs.add((Math.random() * -10) - 300);
        }

        smooth = filter.smooth(obs.toArray(new Double[0]));
        assertEquals(-305, smooth, 1.5);

        obs = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            obs.add((Math.random() * -10) - 77);
        }

        smooth = filter.smooth(obs.toArray(new Double[0]));
        assertEquals(-82, smooth, 1.5);

        obs = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            obs.add((Math.random() * -10) - 150);
        }

        smooth = filter.smooth(obs.toArray(new Double[0]));
        assertEquals(-155, smooth, 1.5);

        obs = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            obs.add((Math.random() * -10) - 40);
        }

        smooth = filter.smooth(obs.toArray(new Double[0]));
        assertEquals(-45, smooth, 1.5);
    }
}
