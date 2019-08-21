package fi.helsinki.ubipositioning.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import fi.helsinki.ubimqtt.IUbiActionListener;
import fi.helsinki.ubimqtt.IUbiMessageListener;
import fi.helsinki.ubimqtt.UbiMqtt;

/**
 * Implementation of interface to provide consumer possibility to
 * get all the messages that have been sent to subscribed topic after start up
 * and to publish data in messages into a another topic.
 */
public class MqttService implements IMqttService {
    UbiMqtt instance;

    private String subscribeTopic;
    private String publishTopic;
    private IMessageListener listener;

    /**
     * Creates instance of the class.
     *
     * @param mqttUrl Mqtt servers URL without '://' start.
     * @param subscribeTopic Topic to subscribe to so that data can be received as messages.
     * @param publishTopic Topic to publish data as messages to.
     */
    public MqttService(String mqttUrl, String subscribeTopic, String publishTopic) {
        this.subscribeTopic = subscribeTopic;
        this.publishTopic = publishTopic;
        instance = new UbiMqtt(mqttUrl);
    }

    /**
     * Creates instance of the class.
     *
     * @param mqttUrl Mqtt servers URL without '://' start.
     * @param subscribeTopic Topic to subscribe to so that data can be received as messages.
     * @param publishTopic Topic to publish data as messages to.
     * @param listener Handler to be called when message arrives through subscription.
     */
    public MqttService(String mqttUrl, String subscribeTopic, String publishTopic, IMessageListener listener) {
        this.subscribeTopic = subscribeTopic;
        this.publishTopic = publishTopic;
        this.listener = listener;
        instance  = new UbiMqtt(mqttUrl);
    }

    /**
     * Connects to MQTT bus to subscribe into subscribeTopic and start listening it.
     * Using defined listener to handle all the messages that comes to the topic after this method is called.
     *
     * @throws NullPointerException if listener is not defined.
     * @throws RuntimeException with problem related throwable as message
     *         if either mqtt bus url or subscribeTopic is badly defined.
     */
    @Override
    public void connect() {
        connect(listener);
    }

    /**
     * Connects to MQTT bus to subscribe into subscribeTopic and start listening it.
     * Using given listener to handle all the messages that comes to the topic after this method is called.
     *
     * @param listener Handler to be called when message arrives through subscription.
     *
     * @throws NullPointerException if listener is not defined.
     * @throws RuntimeException with problem related throwable as message
     *         if either mqtt bus url or subscribeTopic is badly defined.
     */
    @Override
    public void connect(IMessageListener listener) {
        if (listener == null) {
            throw new NullPointerException("IMessageListener not set");
        }

        instance.connect(new IUbiActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                instance.subscribe(subscribeTopic, new IUbiMessageListener() {
                    @Override
                    public void messageArrived(String topic, MqttMessage mqttMessage, String listenerId) {
                        listener.messageArrived(mqttMessage.toString());
                    }
                }, new IUbiActionListener() {
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
     * Connects to MQTT bus to subscribe into subscribeTopic and start listening it.
     * Using defined listener to handle all the messages that comes to the topic after this method is called
     * and are been signed by the right keypair.
     *
     * @param publicKey Key that is used to verify sender of the message.
     *
     * @throws NullPointerException if listener is not defined.
     * @throws RuntimeException with problem related throwable as message
     *         if either mqtt bus url or subscribeTopic is badly defined.
     */
    @Override
    public void connectSigned(String publicKey) {
        connectSigned(publicKey, listener);
    }

    /**
     * Connects to MQTT bus to subscribe into subscribeTopic and start listening it.
     * Using given listener to handle all the messages that comes to the topic after this method is called
     * and are been signed by the right keypair.
     *
     * @param publicKey Key that is used to verify sender of the message.
     * @param listener Handler to be called when message arrives through subscription.
     *
     * @throws NullPointerException if listener is not defined.
     * @throws RuntimeException with problem related throwable as message
     *         if either mqtt bus url or subscribeTopic is badly defined.
     */
    @Override
    public void connectSigned(String publicKey, IMessageListener listener) {
        if (listener == null) {
            throw new NullPointerException("IMessageListener not set");
        }

        instance.connect(new IUbiActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                instance.subscribeSigned(subscribeTopic, new String[]{publicKey}, new IUbiMessageListener() {
                    @Override
                    public void messageArrived(String topic, MqttMessage mqttMessage, String listenerId) {
                        listener.messageArrived(mqttMessage.toString());
                    }
                }, new IUbiActionListener() {
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
     * Connects to MQTT bus to subscribe into subscribeTopic and start listening it.
     * Using defined listener to handle all the messages that comes to the topic after this method is called.
     * Also decrypts the messages with secret key before giving it to the listener.
     *
     * @param secretKey Key that is used to decrypt the messages.
     *
     * @throws NullPointerException if listener is not defined.
     * @throws RuntimeException with problem related throwable as message
     *         if either mqtt bus url or subscribeTopic is badly defined.
     */
    @Override
    public void connectEncrypted(String secretKey) {
        connectEncrypted(secretKey, listener);
    }

    /**
     * Connects to MQTT bus to subscribe into subscribeTopic and start listening it.
     * Using defined listener to handle all the messages that comes to the topic after this method is called.
     * Also decrypts the messages with secret key before giving it to the listener.
     *
     * @param secretKey Key that is used to decrypt the messages.
     * @param listener Handler to be called when message arrives through subscription.
     *
     * @throws NullPointerException if listener is not defined.
     * @throws RuntimeException with problem related throwable as message
     *         if either mqtt bus url or subscribeTopic is badly defined.
     */
    @Override
    public void connectEncrypted(String secretKey, IMessageListener listener) {
        if (listener == null) {
            throw new NullPointerException("IMessageListener not set");
        }

        instance.connect(new IUbiActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                instance.subscribeEncrypted(subscribeTopic, new String[]{secretKey}, new IUbiMessageListener() {
                    @Override
                    public void messageArrived(String topic, MqttMessage mqttMessage, String listenerId) {
                        listener.messageArrived(mqttMessage.toString());
                    }
                }, new IUbiActionListener() {
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
     * Closes session in MQTT server.
     *
     * @throws RuntimeException if disconnecting failed.
     */
    @Override
    public void disconnect() {
        instance.disconnect(new IUbiActionListener() {
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
     * Publish a new message to publishTopic in MQTT bus.
     *
     * @param message Data that wanted to be published into the MQTT bus topic.
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
     * Publish a new signed message to publishTopic in MQTT bus.
     *
     * @param message Data that wanted to be published into the MQTT bus topic.
     * @param secretKey Key used to sign the message so that receiver can verify the sender.
     *
     * @throws RuntimeException if publishTopic is not valid.
     */
    @Override
    public void publishSigned(String message, String secretKey) {
        instance.publishSigned(publishTopic, message, secretKey, new IUbiActionListener() {
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
     * Publish message in encrypted form to publishTopic in MQTT bus.
     *
     * @param message Message to encrypt and publish.
     * @param publicKey Key used to encrypt the message.
     *
     * @throws RuntimeException if publishTopic is not valid.
     */
    @Override
    public void publishEncrypted(String message, String publicKey) {
        instance.publishEncrypted(publishTopic, message, publicKey, new IUbiActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                throw new RuntimeException(exception);
            }
        });
    }
}
