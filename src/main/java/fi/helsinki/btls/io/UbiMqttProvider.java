package fi.helsinki.btls.io;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import fi.helsinki.ubimqtt.IUbiActionListener;
import fi.helsinki.ubimqtt.IUbiMessageListener;
import fi.helsinki.ubimqtt.UbiMqtt;

/**
 * Wrapper for UbiMqtt class.
 */
public class UbiMqttProvider implements IMqttProvider {
    private final UbiMqtt instance;
    private final String subscribeTopic;
    private final String publishTopic;
    private IUbiMessageListener listener;

    /**
     * Wrapper for UbiMqtt class.
     * @param subscribeTopic subscribeTopic to listen
     */
    public UbiMqttProvider(String subscribeTopic, String publishTopic) {
        this.subscribeTopic = subscribeTopic;
        this.publishTopic = publishTopic;
        instance  = new UbiMqtt("localhost");
    }

    /**
     * Connects to Mqtt bus.
     */
    @Override
    public void connect() {
        if (listener == null) {
            throw new NullPointerException("MessageListener not set");
        }
        instance.connect(new IUbiActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                System.out.println("Connected to");
                instance.subscribe(subscribeTopic, listener, new IUbiActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        System.out.println("Subscribed to " + subscribeTopic);
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
    @Override
    public void publish(String message) {
        instance.publish(publishTopic, message, new IUbiActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                System.out.println("Published message to " + publishTopic);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                System.out.println("Publish to " + publishTopic + " failed");
            }
        });
    }

    /**
     * Adds IUbiMessageListener.
     * @param listener listener to add.
     */
    @Override
    public void setListener(IUbiMessageListener listener) {
        this.listener = listener;
    }

}
