package fi.helsinki.btls.io;

import fi.helsinki.ubimqtt.IUbiActionListener;
import fi.helsinki.ubimqtt.UbiMqtt;
import org.eclipse.paho.client.mqttv3.IMqttToken;

public class ConnectionListener implements IUbiActionListener {
    private UbiMqtt mqtt;
    private String topic;

    public ConnectionListener(UbiMqtt mqtt, String topic) {
        this.mqtt = mqtt;
        this.topic = topic;
    }
    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        System.out.println("Connected");
        mqtt.subscribe(topic, new MessageListener(), new SubscriptionListener());
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        System.out.println("Connection failed: " + exception.getMessage());
    }
}
