package fi.helsinki.btls.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import fi.helsinki.btls.datamodels.*;

/**
 * Test class for making sure that data models work as they should.
 */
public class TestDataModels {
    @Test
    public void testEmptyConstructor2D() {
        Location model = new Location2D();

        assertEquals(model.getClass(), Location2D.class);
        assertNull(model.getBeaconId());
        assertEquals(0.0, model.getX(), 0.000000001);
        assertEquals(0.0, model.getXr(), 0.000000001);
        assertEquals(0.0, ((Location2D) model).getAlignment(), 0.000000001);
    }

    @Test
    public void testEmptyConstructor3D() {
        Location model = new Location3D();

        assertEquals(model.getClass(), Location3D.class);
        assertNull(model.getBeaconId());
        assertEquals(0.0, model.getX(), 0.000000001);
        assertEquals(0.0, model.getXr(), 0.000000001);
        assertEquals(0.0, ((Location3D) model).getZ(), 0.000000001);
    }

    @Test
    public void testFullConstructor2D() {
        Location model = get2Dmodel("test beacon", 100, 200.134, 3.567, 0.1, 0);

        assertEquals(model.getClass(), Location2D.class);
        assertEquals("test beacon", model.getBeaconId());
        assertEquals(100, model.getX(), 0.000000001);
        assertEquals(3.567, model.getXr(), 0.000000001);
        assertEquals(0.0, ((Location2D) model).getAlignment(), 0.000000001);
    }

    @Test
    public void testFullConstructor3D() {
        Location model = get3Dmodel("fail", 1, 4, 5.1212, 6, 2, 3);

        assertEquals(model.getClass(), Location3D.class);
        assertEquals("fail", model.getBeaconId());
        assertEquals(1, model.getX(), 0.000000001);
        assertEquals(6, model.getXr(), 0.000000001);
        assertEquals(5.1212, ((Location3D) model).getZ(), 0.000000001);
    }

    @Test
    public void equalsWorks2Dand3D() {
        Location one = get2Dmodel("ez", 0, 1, 0.2, 0.001, 0.723);
        Location two = get2Dmodel("ez", 0, 1, 0.2, 0.001, 0.723);
        Location three = get2Dmodel("notEz", 0, 1, 0.001, 0.234, 5.1);

        Location uno = get3Dmodel("notEz", 0, 1, 5, 0.001, 0.234, 0.1);
        Location dos = get3Dmodel("notEz", 0, 1, 5, 0.001, 0.234, 0.1);
        Location tres = get3Dmodel("ez", 0, 1, 0.723, 0.2, 0.001, 0.723);

        assertEquals(one, two);
        assertEquals(uno, dos);

        assertNotEquals(one, tres);
        assertNotEquals(uno, tres);

        assertNotEquals(one, three);
        assertNotEquals(uno, three);

        assertNotEquals(one, uno);
        assertNotEquals(three, tres);
    }

    @Test
    public void hashCodeWorks2Dand3D() {
        Location one = get2Dmodel("ez", 0, 1, 0.2, 0.001, 0.723);
        Location two = get2Dmodel("ez", 0, 1, 0.2, 0.001, 0.723);
        Location three = get2Dmodel("notEz", 0, 1, 0.001, 0.234, 5.1);

        Location uno = get3Dmodel("notEz", 0, 1, 5, 0.001, 0.234, 0.1);
        Location dos = get3Dmodel("notEz", 0, 1, 5, 0.001, 0.234, 0.1);
        Location tres = get3Dmodel("ez", 0, 1, 0.723, 0.2, 0.001, 0.723);

        assertEquals(one.hashCode(), two.hashCode());
        assertEquals(uno.hashCode(), dos.hashCode());

        assertNotEquals(one.hashCode(), tres.hashCode());
        assertNotEquals(uno.hashCode(), tres.hashCode());

        assertNotEquals(one.hashCode(), three.hashCode());
        assertNotEquals(uno.hashCode(), three.hashCode());

        assertNotEquals(one.hashCode(), uno.hashCode());
        assertNotEquals(three.hashCode(), tres.hashCode());
    }

    @Test
    public void changesEffectEqualsAndHashCode() {
        Location2D one = get2Dmodel("ez", 0, 1, 0.2, 0.001, 0.723);
        Location2D two = get2Dmodel("ez", 0, 1, 0.2, 0.001, 0.723);

        Location3D uno = get3Dmodel("notEz", 0, 1, 5, 0.001, 0.234, 0.1);
        Location3D dos = get3Dmodel("notEz", 0, 1, 5, 0.001, 0.234, 0.1);

        assertEquals(one, two);
        assertEquals(uno, dos);
        assertEquals(one.hashCode(), two.hashCode());
        assertEquals(uno.hashCode(), dos.hashCode());

        one.setY(7.9);
        one.setAlignment(-12.3);
        two.setXr(2);
        two.setYr(300);

        uno.setY(12);
        uno.setZr(-0.0034);
        dos.setZ(-2.1);
        dos.setBeaconId("moi");

        assertNotEquals(one, two);
        assertNotEquals(uno, dos);
        assertNotEquals(one.hashCode(), two.hashCode());
        assertNotEquals(uno.hashCode(), dos.hashCode());
    }

    @Test
    public void toStringWorks() {
        String ez = get2Dmodel("ez", 234.1, 476.2323, 0.3145, 0.001, -0.723).toString();
        String d2 = new Location2D().toString();

        String notEz = get3Dmodel("notEz", 91.123876, 45.78, 23, 0.0023, -0.5768, 0).toString();
        String d3 = new Location3D().toString();

        assertTrue(ez.contains("beaconId=ez"));
        assertTrue(ez.contains("xr=0.3145"));
        assertTrue(d2.contains("alignment=0.0"));
        assertTrue(d2.contains("y=0.0"));

        assertFalse(ez.contains("z=2.456"));
        assertFalse(d2.contains("zr=-0.11"));

        assertTrue(notEz.contains("x=91.123876"));
        assertTrue(notEz.contains("yr=-0.5768"));
        assertTrue(d3.contains("beaconId=null"));
        assertTrue(d3.contains("z=0.0"));

        assertFalse(notEz.contains("alignment=-23.111"));
        assertFalse(d3.contains("alignment=0.0"));
    }

    private Location3D get3Dmodel(String notEz, double x, double y, double z, double x2, double y2, double z2) {
        return new Location3D(notEz, x, y, z, x2, y2, z2);
    }

    private Location2D get2Dmodel(String ez, double x, double y, double x2, double y2, double a) {
        return new Location2D(ez, x, y, x2, y2, a);
    }

    @Test
    public void testBeaconsObservationAdd() {
        Beacon beacon = new Beacon("ATS-1");
        assertEquals(Double.MAX_VALUE, beacon.getMinRSSI(), 0.0000001);

        double min =  Double.MAX_VALUE;
        assertEquals(min, beacon.getMinRSSI(), 0.000001);

        List<Observation> observations = createObservations();
        beacon.setObservations(observations);

        min = Double.MAX_VALUE;
        for (Observation obs : observations) {
            min = min <= obs.getRssi() ? min : obs.getRssi();
        }

        assertEquals(min, beacon.getMinRSSI(), 0.000001);

        observations = createObservations();
        beacon.setObservations(observations);

        min = beacon.getMinRSSI();
        for (Observation obs : observations) {
            min = min <= obs.getRssi() ? min : obs.getRssi();
        }

        assertEquals(min, beacon.getMinRSSI(), 0.000001);

        observations = createObservations();
        beacon.setObservations(observations);

        min = beacon.getMinRSSI();
        for (Observation obs : observations) {
            min = min <= obs.getRssi() ? min : obs.getRssi();
        }

        assertEquals(min, beacon.getMinRSSI(), 0.000001);
    }

    private List<Observation> createObservations() {
        return IntStream.range(0, 50)
                .mapToObj(i -> createObservation("beacon-" + i, "rasp-" + i, Math.random() * 10000))
                .collect(Collectors.toList());
    }

    @Test
    public void testBeaconModel() {
        Beacon b1 = createBeacon("STA-12");
        Beacon b2 = createBeacon("STA-12");
        Beacon b3 = createBeacon("w21");

        assertEquals(b1, b2);
        assertNotEquals(b1, b3);

        assertEquals(b1.hashCode(), b2.hashCode());
        assertNotEquals(b2.hashCode(), b3.hashCode());

        b2.setMinRSSI(300.21);
        b3.getObservations().add(createObservation("rasp", "beacon", 43.001));

        Beacon b4 = createBeacon("w21");

        assertNotEquals(b1, b2);
        assertNotEquals(b4, b3);

        assertNotEquals(b1.hashCode(), b2.hashCode());
        assertNotEquals(b4.hashCode(), b3.hashCode());

        assertTrue(b3.toString().contains("id=w21"));
        assertTrue(b1.toString().contains("minRSSI=0"));
    }

    private Beacon createBeacon(String s) {
        return new Beacon(s, 0, new ArrayList<>(), 30);
    }

    private Observation createObservation(String rasp, String beacon, double v) {
        return new Observation(rasp, beacon, v, LocalDateTime.now());
    }
    private Observation createObservation(String rasp, String beacon, double v, LocalDateTime dateTime) {
        return new Observation(rasp, beacon, v, dateTime);
    }

    @Test
    public void testObservationModel() {
        LocalDateTime dateTime = LocalDateTime.now();
        Observation b1 = createObservation("STA-12", "twin", -56, dateTime);
        Observation b2 = createObservation("STA-12", "twin", -56, dateTime);
        Observation b3 = createObservation("w21", "lonely", -90, dateTime);

        assertEquals(b1, b2);
        assertNotEquals(b1, b3);

        assertEquals(b1.hashCode(), b2.hashCode());
        assertNotEquals(b2.hashCode(), b3.hashCode());

        b2.setBeaconId("troll");
        b3.setRssi(0);

        Observation b4 = createObservation("w21", "lonely", -90);

        assertNotEquals(b1, b2);
        assertNotEquals(b4, b3);

        assertNotEquals(b1.hashCode(), b2.hashCode());
        assertNotEquals(b4.hashCode(), b3.hashCode());

        assertTrue(b3.toString().contains("raspId=w21"));
        assertTrue(b1.toString().contains("rssi=-56"));
    }

    @Test
    public void testObserverModel() {
        Observer b1 = createObserver("STA-12", 200, 1000);
        Observer b2 = createObserver("STA-12", 200, 1000);
        Observer b3 = createObserver("w21", 5756, 0);

        assertEquals(b1, b2);
        assertNotEquals(b1, b3);

        assertEquals(b1.hashCode(), b2.hashCode());
        assertNotEquals(b2.hashCode(), b3.hashCode());

        b2.setObserverId("troll");
        b3.setPosition(new double[]{7233, 5748});

        Observer b4 = createObserver("w21", 5756, -90);

        assertNotEquals(b1, b2);
        assertNotEquals(b4, b3);

        assertNotEquals(b1.hashCode(), b2.hashCode());
        assertNotEquals(b4.hashCode(), b3.hashCode());

        assertTrue(b3.toString().contains("observerId=w21"));
        assertTrue(b1.toString().contains("position"));
    }

    private Observer createObserver(String rasp, double x, double y) {
        return new Observer(rasp, x, y);
    }
}
