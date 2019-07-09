package fi.helsinki.btls;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import fi.helsinki.btls.datamodels.Observer;

/**
 * Test class for observer service.
 */
public class ObserverServiceTest {
    @Test
    public void addingObserverSuccess() {
        IObserverService s = new ObserverService(2);
        Observer observer = createObserver("test", 1, 2);

        assertTrue(s.addObserver(observer));
        assertEquals(1, s.getAllObservers().size());

        Observer isItSame = s.getObserver(observer.getObserverId());
        assertNotNull(isItSame);
        assertEquals(observer.getPosition().length, isItSame.getPosition().length);
    }

    @Test
    public void addingObserverFails() {
        IObserverService s = new ObserverService(2);
        Observer observer = createObserver("shouldn't be added", 5, 4, 10);

        assertFalse(s.addObserver(observer));
        assertEquals(0, s.getAllObservers().size());

        Observer isItSame = s.getObserver(observer.getObserverId());
        assertNull(isItSame);
    }

    @Test
    public void addingMultipleObserversSuccess() {
        IObserverService s = new ObserverService(2);

        for (int i = 1; i < Math.pow(2, 11); i *= 2) {
            Observer temp = createObserver("rasp-" + i, i - 3, -i + 3);
            assertTrue(s.addObserver(temp));
        }

        assertEquals(11, s.getAllObservers().size());
    }

    @Test
    public void notAllAreAdded() {
        IObserverService s = new ObserverService(2);

        for (int i = 1; i < Math.pow(2, 6); i *= 2) {
            Observer temp = createObserver(Double.toString(Math.random() * 55), i - 3, -i + 3);
            assertTrue(s.addObserver(temp));
        }

        for (int i = 1; i < Math.pow(2, 11); i *= 2) {
            Observer temp = createObserver("shouldn't be-" + i, i - 3, 12.4567, Math.random() * 200);
            assertFalse(s.addObserver(temp));
        }

        for (int i = 1; i < Math.pow(2, 5); i *= 2) {
            Observer temp = createObserver(UUID.randomUUID().toString(), 0.0012, Math.random() * -i);
            assertTrue(s.addObserver(temp));
        }

        assertEquals(11, s.getAllObservers().size());
    }

    @Test
    public void canDeleteObserver() {
        IObserverService s = new ObserverService(3);

        for (long i = 1; i < Math.pow(2, 20); i *= 2) {
            Observer temp = createObserver(Double.toHexString(i), i - 3, 12.4567, Math.random() * 200);
            assertTrue(s.addObserver(temp));
        }

        assertEquals(20, s.getAllObservers().size());
        Observer observer = s.deleteObserver(Double.toHexString(512));
        assertEquals(19, s.getAllObservers().size());

        assertEquals(3, observer.getPosition().length);
        assertEquals(509, observer.getPosition()[0], 0.000001);
    }

    @Test
    public void canDeleteMultipleObserver() {
        IObserverService s = new ObserverService(2);

        for (long i = 1; i < Math.pow(2, 20); i *= 2) {
            Observer temp = createObserver(Double.toHexString(i), Math.random(), 777);
            assertTrue(s.addObserver(temp));
        }

        assertEquals(20, s.getAllObservers().size());

        for (long i = 1; i < Math.pow(2, 20); i *= 4) {
            assertNotNull(s.deleteObserver(Double.toHexString(i)));
        }

        assertEquals(10, s.getAllObservers().size());
    }

    @Test
    public void canDeleteAlLObservers() {
        IObserverService s = new ObserverService(3);

        for (long i = 1; i < Math.pow(2, 15); i *= 2) {
            Observer temp = createObserver(Double.toHexString(i), i - 3, 12.4567, Math.random() * 200);
            assertTrue(s.addObserver(temp));
        }

        assertEquals(15, s.getAllObservers().size());
        s.deleteAllObservers();
        assertEquals(0, s.getAllObservers().size());

        Observer observer = createObserver("test", 12, 0.002, -18);
        assertTrue(s.addObserver(observer));
        assertEquals(1, s.getAllObservers().size());
    }

    @Test
    public void canAddAllObserversSameTime() {
        IObserverService s = new ObserverService(3);
        List<Observer> all = new ArrayList<>();

        for (long i = 1; i < Math.pow(2, 12); i *= 2) {
            Observer temp = createObserver("rasp-" + i, 3.145, Math.random(), -i);
            all.add(temp);
        }

        assertTrue(s.addAllObservers(all));
        assertEquals(12, s.getAllObservers().size());
    }

    @Test
    public void addingAllFails() {
        IObserverService s = new ObserverService(3);
        List<Observer> all = new ArrayList<>();

        for (long i = 1; i < Math.pow(2, 3); i *= 2) {
            Observer temp = createObserver(UUID.randomUUID().toString(), 3.145, Math.random(), -i);
            all.add(temp);
        }

        for (long i = 1; i < Math.pow(2, 7); i *= 2) {
            Observer temp = createObserver("rasp-" + i, Math.random(), -i);
            all.add(temp);
        }

        all.add(createObserver("w", 1, 1, 1));

        assertFalse(s.addAllObservers(all));
        assertEquals(0, s.getAllObservers().size());
    }

    private Observer createObserver(String s, double v, double v2) {
        return new Observer(s, v, v2);
    }

    private Observer createObserver(String s, double v, double v2, double v3) {
        return new Observer(s, v, v2, v3);
    }
}
