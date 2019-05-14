package fi.helsinki.btls.io;

import fi.helsinki.ubimqtt.IUbiMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MessageListener implements IUbiMessageListener {

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage, String listenerId) throws Exception {
        System.out.println(topic + ": " + mqttMessage.toString());
    }
}
