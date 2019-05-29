package fi.helsinki.btls.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import fi.helsinki.btls.domain.LocationModel;
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
        LocationModel location = new LocationModel("raspi", 1, 1, 1, 1, 1, 1);
        service.publish(location);
        inOrder.verify(provider).publish(location.toString());
    }
}
