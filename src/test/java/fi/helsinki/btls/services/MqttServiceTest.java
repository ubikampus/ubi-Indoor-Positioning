package fi.helsinki.btls.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.google.gson.Gson;
import fi.helsinki.ubimqtt.IUbiMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import fi.helsinki.btls.domain.LocationModel;
import fi.helsinki.btls.io.UbiMqttProvider;

/**
 * Test class for MqttService.
 */
public class MqttServiceTest {
    private UbiMqttProvider provider;
    private IMqttService service;

    @Before
    public void setUp() throws Exception {
        provider = new UbiMqttProvider();
        service = new MqttService(provider, new Gson());
    }

    @Test
    public void newServiceConnectsAndSubscribes() throws InterruptedException {
        UbiMqttProvider test = new UbiMqttProvider();
        test.setListener((topic, mqttMessage, listenerId) -> {
            try {
                System.out.println(topic + ": " + mqttMessage.toString());
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        });

        test.connect();

        Thread.sleep(5000);
    }

    @Test
    public void publishCallsProviderPublish() throws InterruptedException {
        LocationModel location = new LocationModel("raspi", 1, 1, 1, 1);
        service.publish(location);

        Thread.sleep(5000);
    }
}
