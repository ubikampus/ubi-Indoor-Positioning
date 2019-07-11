package fi.helsinki.ubipositioning.mqtt;

import fi.helsinki.ubimqtt.IUbiMessageListener;

/**
 * Interface for MqttProvider.
 */
public interface IMqttProvider {
    void publish(String message);
    void setListener(IUbiMessageListener listener);
    void connect();
}
