package fi.helsinki.btls.io;

import fi.helsinki.ubimqtt.IUbiActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

public class SubscriptionListener implements IUbiActionListener {
    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        System.out.println("Subscribed");
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        System.out.println("Subscription failed: " + exception.getMessage());
    }
}
