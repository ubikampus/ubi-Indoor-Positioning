package fi.helsinki.ubipositioning.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import fi.helsinki.ubimqtt.IUbiMessageListener;
import fi.helsinki.ubipositioning.datamodels.Observation;

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
            listener.messageArrived("", serialize(new Observation("rasp-1", "1", -80)), "");
            listener.messageArrived("", serialize(new Observation("rasp-2", "1", -80)), "");
            listener.messageArrived("", serialize(new Observation("rasp-3", "1", -80)), "");
            listener.messageArrived("", serialize(new Observation("rasp-4", "1", -80)), "");
            listener.messageArrived("", serialize(new Observation("rasp-1", "2", -80)), "");
            listener.messageArrived("", serialize(new Observation("rasp-2", "2", -80)), "");
            listener.messageArrived("", serialize(new Observation("rasp-3", "2", -80)), "");
            listener.messageArrived("", serialize(new Observation("rasp-4", "2", -80)), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MqttMessage serialize(Observation observation) {
        MqttMessage message =  new MqttMessage();
        String json = "{ \"raspId\": \"" + observation.getRaspId()
                + "\", \"beaconId\": \"" + observation.getBeaconId()
                + "\", \"volume\": \"" + observation.getRssi() + "\" }";
        message.setPayload(json.getBytes());
        return message;
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
