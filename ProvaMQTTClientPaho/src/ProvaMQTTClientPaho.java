import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.*;
 
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ProvaMQTTClientPaho {
	private static final String MY_DEVICE_ID = "xxx";

	private static final String MQTT_HOST = "xxx";
	private static final int MQTT_PORT = 11584;
	private static final String MQTT_USER = "xxx";
	private static final String MQTT_PASS = "xx";
	
	private static final String DEVICE_ID = "xxx";
	private static final String DEVICE_ID1 = "xxx";
	private static final String HANA_CLOUD_NAME = "xxx";
	
	public static void main(String[] args) throws MqttPersistenceException, MqttException, InterruptedException{
		
		IotMMSJsonManager manager = new IotMMSJsonManager(MQTT_HOST, MQTT_PORT, MQTT_USER, MQTT_PASS);

		while (true) {

			IotMMSJsonMessage iMex = manager.generateMex();

			Gson gson = new GsonBuilder().create();

			System.out.println("Genereted message: " + gson.toJson(iMex));
			Random r = new Random();
			String topic = "";
			if (r.nextBoolean())
				topic = manager.generateTopic(HANA_CLOUD_NAME, DEVICE_ID);
			else
				topic = manager.generateTopic(HANA_CLOUD_NAME, DEVICE_ID1);

			manager.sendMex(topic, iMex);

			System.out.println("   ---> SENDED to topic: " + topic);
			
			// ASPETTO 5 secondi
			TimeUnit.SECONDS.sleep(300);
			
			
		}
	}

}
