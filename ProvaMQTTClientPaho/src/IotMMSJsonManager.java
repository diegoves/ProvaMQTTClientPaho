import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class IotMMSJsonManager {
	private static final String TOPIC_TEMPLATE = "iot/data/iotmms<HANA_CLOUD_NAME>/v1/<MY_DEVICE_ID>";
	private static final String MEX_MODE = "async";
	private MqttClient client;
	
	public IotMMSJsonManager() {
	}
	
	public IotMMSJsonManager(String host, int port, String user, String pass) throws MqttException {
		this.connect(host, port, user, pass);
	}
	
	public IotMMSJsonMessage generateMex() {
		Message mex = new Message();
		Random r = new Random();
		
		mex.setDato((int)(r.nextDouble()*100));
		
		IotMMSJsonMessage iotMex = new IotMMSJsonMessage();
		iotMex.setMessageType(Message.MESSAGE_TYPE);
		iotMex.setMode(MEX_MODE);

		List<Message> mexs = new ArrayList<Message>();
		mexs.add(mex);

		iotMex.setMessages(mexs);

		return iotMex;
	}
	
	public void sendMex(String topic, IotMMSJsonMessage iMex) throws MqttPersistenceException, MqttException {

		Gson gson = new GsonBuilder().create();
		String sMex = gson.toJson(iMex);

		if (isConnected()) {
			MqttMessage mMex = new MqttMessage();
			mMex.setPayload(sMex.getBytes(Charset.forName("UTF-8")));
			mMex.setQos(0);
			mMex.setRetained(false);
			client.publish(topic, mMex);
		} else {
			System.out.println("ERR : No MQTT Connection");
		}
	}

	
	private boolean isConnected() {
		return client != null || client.isConnected();
	}
	
	public void connect(String host, int port, String user, String pass) throws MqttException {
		client = new MqttClient("tcp://" + host + ":" + port, user);

		MqttConnectOptions options = new MqttConnectOptions();
		options.setUserName(user);
		options.setPassword(pass.toCharArray());

		client.connect(options);
	}
	
	public String generateTopic(String hanaCloudName, String myDeviceId) {
		String topic = TOPIC_TEMPLATE.replace("<HANA_CLOUD_NAME>", hanaCloudName).replace("<MY_DEVICE_ID>", myDeviceId);
		return topic;
	}
}
