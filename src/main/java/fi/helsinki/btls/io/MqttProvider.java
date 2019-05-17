package fi.helsinki.btls.io;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import fi.helsinki.ubimqtt.IUbiActionListener;
import fi.helsinki.ubimqtt.IUbiMessageListener;
import fi.helsinki.ubimqtt.UbiMqtt;

/**
 * Wrapper for UbiMqtt class.
 */
public class MqttProvider {
    private final UbiMqtt instance;
    private final String topic;
    private final IUbiMessageListener listener;

    /**
     * Wrapper for UbiMqtt class.
     * @param topic topic to listen
     * @param listener IUbiMessageListener fro subscribing
     */
    public MqttProvider(String topic, IUbiMessageListener listener) {
        this.topic = topic;
        this.listener = listener;
        instance  = new UbiMqtt("iot.ubikampus.net");
        connect();
    }

    private void connect() {
        instance.connect(new IUbiActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                System.out.println("Connected to");
                instance.subscribe(topic, listener, new IUbiActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        System.out.println("Subscribed to " + topic);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        System.out.println("Subscription failed: " + exception.toString());
                    }
                });
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                System.out.println("Connection failed: " + exception.toString());
            }
        });
    }

    /**
     * Publish message.
     * @param message
     */
    public void publish(String message) {
        instance.publish(topic, message, new IUbiActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                System.out.println("Published message to " + topic);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                System.out.println("Publish to " + topic + " failed");
            }
        });
    }

}
