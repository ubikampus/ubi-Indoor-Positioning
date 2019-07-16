package fi.helsinki.ubipositioning.mqtt;

import fi.helsinki.ubimqtt.IUbiMessageListener;

/**
 * Interface to hide actual mqtt protocol from service that uses it to serve consumers.
 */
public interface IMqttService {
    void publish(String message);
    void PublishSigned(String message, String secretKey);
    void connect();
    void connect(IUbiMessageListener listener);
    void connectSigned(String publicKey);
    void connectSigned(String publicKey, IUbiMessageListener listener);
    void disconnect();
}
