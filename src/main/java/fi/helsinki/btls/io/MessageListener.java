package fi.helsinki.btls.io;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import fi.helsinki.ubimqtt.IUbiMessageListener;

/**
 * Messagelistener for UbiMqtt.
 */
public class MessageListener implements IUbiMessageListener {

    /**
     * Messagelistener.
     * @param topic
     * @param mqttMessage
     * @param listenerId
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage, String listenerId) throws Exception {
        System.out.println(topic + ": " + mqttMessage.toString());
    }
}
