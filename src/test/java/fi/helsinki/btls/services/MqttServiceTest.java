package fi.helsinki.btls.services;

import com.google.gson.Gson;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.io.UbiMqttProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    public void NewServiceConnectsAndSubscribes() {

        inOrder.verify(provider).setListener(any());
        inOrder.verify(provider).connect();
    }

    @Test
    public void PublishCallsProviderPublish() {
        LocationModel location = new LocationModel("raspi", 1, 1, 1,1);
        service.publish(location);
        inOrder.verify(provider).publish(location.toString());
    }
}
