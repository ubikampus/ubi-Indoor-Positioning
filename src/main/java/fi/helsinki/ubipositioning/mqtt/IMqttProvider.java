package fi.helsinki.ubipositioning.mqtt;

import fi.helsinki.ubimqtt.IUbiMessageListener;

/**
 * Interface to hide actual mqtt protocol from service that uses it to serve consumers.
 */
public interface IMqttProvider {
    void publish(String message);
    void setListener(IUbiMessageListener listener);
    void connect();
}
