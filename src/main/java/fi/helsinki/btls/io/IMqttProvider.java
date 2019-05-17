package fi.helsinki.btls.io;

import fi.helsinki.ubimqtt.IUbiMessageListener;

public interface IMqttProvider {
    void publish(String message);
    void setListener(IUbiMessageListener listener);
    void connect();
}
