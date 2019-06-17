package fi.helsinki.btls.services;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import fi.helsinki.btls.domain.ObservationModel;
import fi.helsinki.btls.io.IMqttProvider;
import fi.helsinki.ubimqtt.IUbiMessageListener;

/**
 * Dummy provider for testing.
 */
public class DummyMqttProvider implements IMqttProvider {
    private IUbiMessageListener listener;
    @Override
    public void publish(String message) {

    }

    @Override
    public void setListener(IUbiMessageListener listener) {
        this.listener = listener;
    }

    @Override
    public void connect() {
    }

    public void simulateBus() {
        try {
            listener.messageArrived("", serialize(new ObservationModel("rasp-1", "1", -80)), "");
            listener.messageArrived("", serialize(new ObservationModel("rasp-2", "1", -80)), "");
            listener.messageArrived("", serialize(new ObservationModel("rasp-3", "1", -80)), "");
            listener.messageArrived("", serialize(new ObservationModel("rasp-4", "1", -80)), "");
            listener.messageArrived("", serialize(new ObservationModel("rasp-1", "2", -80)), "");
            listener.messageArrived("", serialize(new ObservationModel("rasp-2", "2", -80)), "");
            listener.messageArrived("", serialize(new ObservationModel("rasp-3", "2", -80)), "");
            listener.messageArrived("", serialize(new ObservationModel("rasp-4", "2", -80)), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MqttMessage serialize(ObservationModel observationModel) {
        MqttMessage message =  new MqttMessage();
        String json = "{ \"raspId\": \"" + observationModel.getRaspId()
                + "\", \"beaconId\": \"" + observationModel.getBeaconId()
                + "\", \"volume\": \"" + observationModel.getRssi() + "\" }";
        message.setPayload(json.getBytes());
        return message;
    }

    @Override
    public boolean isDebugMode() {
        return false;
    }

    public void simulateBusInvalid() {
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload("{\"object\":\"ok\"}".getBytes());
            listener.messageArrived("", message , "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
