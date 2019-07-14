package fi.helsinki.ubipositioning.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import fi.helsinki.ubimqtt.IUbiActionListener;
import fi.helsinki.ubimqtt.IUbiMessageListener;
import fi.helsinki.ubimqtt.UbiMqtt;

/**
 * Implementation of MQTT messaging protocol.
 * Uses UbiMqtt so that most of configuration related stuff is out of the box.
 *
 * @see UbiMqtt
 */
class UbiMqttProvider implements IMqttProvider {
    private final UbiMqtt instance;
    private final String subscribeTopic;
    private final String publishTopic;
    private IUbiMessageListener listener;

    /**
     * Implementation of MQTT bus usage.
     *
     * @param mqttUrl url of the MQTT bus.
     * @param subscribeTopic topic to listen in the MQTT bus.
     * @param publishTopic topic to publish in the MQTT bus.
     */
    public UbiMqttProvider(String mqttUrl, String subscribeTopic, String publishTopic) {
        this.subscribeTopic = subscribeTopic;
        this.publishTopic = publishTopic;
        instance  = new UbiMqtt(mqttUrl);
    }

    /**
     * Connects to MQTT bus to subscribe into subscribeTopic
     * and start listening it.
     * Using defined listener to handle all the messages that comes to the topic after this method is called.
     *
     * @throws NullPointerException if listener is not defined.
     * @throws RuntimeException with problem related throwable as message
     *         if either mqtt bus url or subscribeTopic is badly defined.
     */
    @Override
    public void connect() {
        if (listener == null) {
            throw new NullPointerException("MessageListener not set");
        }

        instance.connect(new IUbiActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                instance.subscribe(subscribeTopic, listener, new IUbiActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        throw new RuntimeException(exception);
                    }
                });
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    /**
     * Publish a new message to publishTopic in MQTT bus.
     *
     * @param message data that wanted to be published into the MQTT bus topic.
     *
     * @throws RuntimeException if publishTopic is not valid.
     */
    @Override
    public void publish(String message) {
        instance.publish(publishTopic, message, new IUbiActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    /**
     * Set a new handler that is called when new message appears into the subscribed topic.
     *
     * @param listener handler to be called when message arrives through subscription.
     */
    @Override
    public void setListener(IUbiMessageListener listener) {
        this.listener = listener;
    }
}
