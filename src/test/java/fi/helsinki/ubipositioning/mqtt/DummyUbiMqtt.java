package fi.helsinki.ubipositioning.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fi.helsinki.ubimqtt.IUbiActionListener;
import fi.helsinki.ubimqtt.IUbiMessageListener;
import fi.helsinki.ubimqtt.UbiMqtt;

/**
 * Mock of UbiMqtt.
 */
class DummyUbiMqtt extends UbiMqtt {
    static Map<String, List<String>> messagesOfTopics = new HashMap<>();
    static boolean connected = false;

    private String serverAddress;

    DummyUbiMqtt(String serverAddress) {
        super(serverAddress);
        this.serverAddress = serverAddress;
    }

    @Override
    public void connect(IUbiActionListener actionListener) {
        if (!serverAddress.contains("://") && !connected) {
            connected = true;
            actionListener.onSuccess(null);
        } else {
            actionListener.onFailure(null, new Throwable("Connection failed!"));
        }
    }

    @Override
    public void disconnect(IUbiActionListener actionListener) {
        if (connected) {
            connected = false;
            actionListener.onSuccess(null);
        } else {
            actionListener.onFailure(null, new Throwable("No connection to close!"));
        }
    }

    @Override
    public void subscribe(String topic, IUbiMessageListener listener, IUbiActionListener actionListener) {
        if (!connected) {
            actionListener.onFailure(null, new Throwable("No connection to server!"));
        }

        try {
            simulateBus(topic, listener);
            actionListener.onSuccess(null);
        } catch (Exception e) {
            actionListener.onFailure(null, e);
        }
    }

    private void simulateBus(String topic, IUbiMessageListener listener) throws Exception {
        if (messagesOfTopics.containsKey(topic)) {
            for (String message : messagesOfTopics.get(topic)) {
                listener.messageArrived(topic, new MqttMessage(message.getBytes()), "");
            }
        }
    }

    @Override
    public void subscribeSigned(String topic, String[] publicKeys, IUbiMessageListener listener, IUbiActionListener actionListener) {
        // Needs mocking.
    }

    @Override
    public void publish(String topic, String message, IUbiActionListener actionListener) {
        if (messagesOfTopics.containsKey(topic)) {
            messagesOfTopics.get(topic).add(message);
        } else {
            List<String> messages = new ArrayList<>();
            messages.add(message);
            messagesOfTopics.put(topic, messages);
        }

        actionListener.onSuccess(null);
    }

    @Override
    public void publishSigned(String topic, String message, String privateKey, IUbiActionListener actionListener) {
        // Needs mocking.
    }
}
