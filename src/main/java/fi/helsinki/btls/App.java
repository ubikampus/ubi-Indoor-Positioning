/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package fi.helsinki.btls;

import com.google.gson.Gson;
import fi.helsinki.btls.domain.Beacon;
import fi.helsinki.btls.io.IMqttProvider;
import fi.helsinki.btls.io.UbiMqttProvider;
import fi.helsinki.btls.services.IMqttService;
import fi.helsinki.btls.services.LocationService;
import fi.helsinki.btls.services.MqttService;

import java.util.List;

public class App {
    public static void main(String[] args) {
        IMqttProvider provider = new UbiMqttProvider();
        IMqttService mqttService = new MqttService(provider, new Gson());
        LocationService service = new LocationService(mqttService);
        while(true) {

            try {
                Thread.sleep(1000);
                List<Beacon> beacons = mqttService.getBeacons();

                for (int i = 0; i < beacons.size(); i++) {
                    mqttService.publish(service.calculateBeaconLocation2D(beacons.get(i)));
                }
                //service.calculateLocation2D();
                }
            catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }
    }
}
