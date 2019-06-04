package fi.helsinki.btls.io;

import fi.helsinki.ubimqtt.IUbiMessageListener;

/**
 * Interface for MqttProvider.
 */
public interface IMqttProvider {
    void publish(String message);
    void setListener(IUbiMessageListener listener);
    void connect();
    boolean isDebugMode();
}
