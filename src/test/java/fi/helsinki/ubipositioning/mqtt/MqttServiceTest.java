package fi.helsinki.ubipositioning.mqtt;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import com.google.gson.Gson;
import org.junit.*;
import org.mockito.InOrder;
import java.util.Collections;
import java.util.List;
import fi.helsinki.ubipositioning.datamodels.*;


/**
 * Test class for MqttService.
 */
public class MqttServiceTest {
    UbiMqttProvider provider;
    IMqttService service;
    InOrder inOrder;

    @Before
    public void setUp() throws Exception {
        provider = mock(UbiMqttProvider.class);
        service = new MqttService(provider);
        inOrder = inOrder(provider);
    }

    @Test
    public void newServiceConnectsAndSubscribes() {
        inOrder.verify(provider).setListener(any());
        inOrder.verify(provider).connect();
    }

    @Test
    public void publishCallsProviderPublish() {
        Location location = new Location2D("raspi", 1, 1, 1, 1, 1);

        service.publish(location);
        inOrder.verify(provider).publish(new Gson().toJson(Collections.singletonList(location)));
    }
    @Test
    public void getBeaconsReturnsBeaconsWhenObservationsArePresent() {
        DummyMqttProvider provider = new DummyMqttProvider();
        service = new MqttService(provider);
        provider.simulateBus();
        List<Beacon> beacons = service.getBeacons();
        assertFalse(beacons.isEmpty());
    }
    @Test
    public void getObservationsReturnsObservationsWhenObservationsArePresent() {
        DummyMqttProvider provider = new DummyMqttProvider();
        service = new MqttService(provider);
        provider.simulateBus();
        List<Observation> obs = service.getObservations();
        assertFalse(obs.isEmpty());
    }
    @Test
    public void listenerHandlesInvalidData() { //TODO(andeem): change system out to ByteArrayOutputStream and verifiy the exception is printed
        DummyMqttProvider provider = new DummyMqttProvider();
        service = new MqttService(provider);
        provider.simulateBusInvalid();
    }
}
