package fi.helsinki.ubipositioning.mqtt;

/**
 * Interface to hide actual mqtt protocol from service that uses it to serve consumers.
 */
public interface IMqttService {
    void publish(String message);
    void publishSigned(String message, String secretKey);
    void publishEncrypted(String message, String publicKey);
    void connect();
    void connect(IMessageListener listener);
    void connectSigned(String publicKey);
    void connectSigned(String publicKey, IMessageListener listener);
    void connectEncrypted(String secretKey);
    void connectEncrypted(String secretKey, IMessageListener listener);
    void disconnect();
}
