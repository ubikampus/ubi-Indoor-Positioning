package fi.helsinki.ubipositioning.mqtt;

/**
 * Injects mocked UbiMqtt to remove dependency of having active MQTT server.
 */
class MqttServiceMock extends MqttService {
    MqttServiceMock(String mqttUrl, String subscribeTopic, String publishTopic) {
        super(mqttUrl, subscribeTopic, publishTopic);
        instance = new DummyUbiMqtt(mqttUrl);
    }

    MqttServiceMock(String mqttUrl, String subscribeTopic, String publishTopic, IMessageListener listener) {
        super(mqttUrl, subscribeTopic, publishTopic, listener);
        instance = new DummyUbiMqtt(mqttUrl);
    }
}
