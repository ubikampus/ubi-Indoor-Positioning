package fi.helsinki.btls.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import com.google.gson.Gson;
import org.junit.*;
import org.mockito.InOrder;
import java.util.List;
import fi.helsinki.btls.domain.Beacon;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.domain.ObservationModel;
import fi.helsinki.btls.io.UbiMqttProvider;


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
        service = new MqttService(provider, new Gson());
        inOrder = inOrder(provider);
    }

    @Test
    public void newServiceConnectsAndSubscribes() {
        inOrder.verify(provider).setListener(any());
        inOrder.verify(provider).connect();
    }

    @Test
    public void publishCallsProviderPublish() {
        LocationModel location = new LocationModel("raspi", 1, 1, 1, 1, 1, 1, 1);

        service.publish(location);
        inOrder.verify(provider).publish(location.toString());
    }
    @Test
    public void getBeaconsReturnsBeaconsWhenObservationsArePresent() {
        DummyMqttProvider provider = new DummyMqttProvider();
        service = new MqttService(provider, new Gson());
        provider.simulateBus();
        List<Beacon> beacons = service.getBeacons();
        assertFalse(beacons.isEmpty());
    }
    @Test
    public void getObservationsReturnsObservationsWhenObservationsArePresent() {
        DummyMqttProvider provider = new DummyMqttProvider();
        service = new MqttService(provider, new Gson());
        provider.simulateBus();
        List<ObservationModel> obs = service.getObservations();
        assertFalse(obs.isEmpty());
    }
    @Test
    public void listenerHandlesInvalidData() { //TODO(andeem): change system out to ByteArrayOutputStream and verifiy the exception is printed
        DummyMqttProvider provider = new DummyMqttProvider();
        service = new MqttService(provider, new Gson());
        provider.simulateBusInvalid();
    }
}
