package fi.helsinki.ubipositioning.mqtt;

/**
 * Listener used to andle messages that are received in MQTT bus.
 */
public interface IMessageListener {
    void messageArrived(String message);
}
