package fi.helsinki.ubipositioning.mqtt;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests to make sure mqtt integration works.
 */
public class MqttServiceTest {
    private MqttService mock;

    private IMessageListener emptyMessageListener = new IMessageListener() {
        @Override
        public void messageArrived(String message) {
        }
    };

    @Before
    public void setUp() {
        DummyUbiMqtt.messagesOfTopics.clear();
        DummyUbiMqtt.connected = false;
    }

    @Test
    public void testConnectSuccessfully() {
        mock = new MqttServiceMock("iot.ubikampus.net", "wantedMessages", "publish/test");
        try {
            assertFalse(DummyUbiMqtt.connected);

            mock.connect(emptyMessageListener);
            assertTrue(DummyUbiMqtt.connected);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void connectFails() {
        mock = new MqttServiceMock("mqtt://mqtt.example.com", "ListenThese", "publish/test");
        try {
            mock.connect(emptyMessageListener);
            fail();
        } catch (NullPointerException e) {
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void connectWithOutListener() {
        mock = new MqttServiceMock("mqtt://mqtt.example.com", "ListenThese", "publish/test");
        try {
            mock.connect();
            fail();
        } catch (NullPointerException e) {
            assertTrue(true);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void testDisconnectSuccessfully() {
        mock = new MqttServiceMock("mqtt.example.com", "data/get", "data/send", emptyMessageListener);
        try {
            assertFalse(DummyUbiMqtt.connected);

            mock.connect();
            assertTrue(DummyUbiMqtt.connected);

            mock.disconnect();
            assertFalse(DummyUbiMqtt.connected);
        } catch (RuntimeException e) {
            fail();
        }
    }

    @Test
    public void disconnectFails() {
        mock = new MqttServiceMock("mqtt.example.com", "data/get", "data/send", emptyMessageListener);
        try {
            mock.disconnect();
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void disconnectFails2() {
        mock = new MqttServiceMock("mqtt://iot.ubikampus.net", "wantedMessages", "publish/test", emptyMessageListener);
        try {
            mock.disconnect();
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void disconnectFails3() {
        mock = new MqttServiceMock("mqtt.example.com", "ListenThese", "publish/test");
        try {
            mock.disconnect();
            fail();
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void connectionMeanSubscribeAndThenData() {
        List<String> messages = new ArrayList<>();
        String subTopic = "ListenThese";
        messages.add("first come in");
        messages.add("second come in");
        messages.add("third come in");
        messages.add("forth come in");
        DummyUbiMqtt.messagesOfTopics.put(subTopic, messages);

        List<String> listenerGot = new ArrayList<>();
        IMessageListener listener = new IMessageListener() {
            @Override
            public void messageArrived(String message) {
                listenerGot.add(message);
            }
        };

        mock = new MqttServiceMock("hail.io", subTopic, "publish/test", listener);
        try {
            mock.connect();
            assertEquals(4, listenerGot.size());
            assertEquals("third come in", listenerGot.get(2));
            assertEquals("first come in", listenerGot.get(0));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void publishAddsData() {
        String subTopic = "test/subscribe";
        String pubTopic = "test/publish";

        mock = new MqttServiceMock("iot.ubikampus.net", subTopic, pubTopic, emptyMessageListener);
        try {
            mock.publish("first publish");
            mock.publish("{'name': 'json'}");

            assertEquals(1, DummyUbiMqtt.messagesOfTopics.size());
            assertTrue(DummyUbiMqtt.messagesOfTopics.containsKey(pubTopic));
            assertNotNull(DummyUbiMqtt.messagesOfTopics.get(pubTopic));
            assertEquals(2, DummyUbiMqtt.messagesOfTopics.get(pubTopic).size());
            assertEquals("first publish", DummyUbiMqtt.messagesOfTopics.get(pubTopic).get(0));
        } catch (Exception e) {
            fail();
        }
    }
}
