package com.unimib.smarthome.broker;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.entity.EntityManager;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.messages.MqttPublishMessage;

public class BrokerManager {

	private Logger logger = LogManager.getLogger();
	final static Level BROKER = Level.forName("BROKER", 350);
	
	static BrokerManager instance;
	private BrokerServer brokerServer;
	private EntityManager entityManager;

	private Map<String, Integer> brokerMap = new HashMap<>();

	private BrokerManager() {
	}

	public static BrokerManager getInstance() {
		if (instance == null) {
			instance = new BrokerManager();
		}
		return instance;
	}

	public void startBrokerServer() {
		try {
			brokerServer = new BrokerServer();
			brokerServer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void registryEntityTopic(String topic, int entityID) throws Exception {
		if(!brokerMap.containsKey(topic)) {
			logger.log(BROKER, "Associated topic [" + topic + "] to entity [" + entityID + "]");
			brokerMap.put(topic, entityID);
		}else {
			throw new Exception("Multiple topic");
		}	
	}
	
	
	//Chiamato quando ricevo un messaggio dal simulatore
	public void dispatchMessage(MqttPublishMessage message) {		
		int entityID = brokerMap.get(message.topicName());
		logger.log(BROKER, "Dispaching message [" + message.payload() + "] to entity " +  entityID);
		entityManager.updateEntity(entityID, message.payload().toString());
	}

	//Chiamato per mandare un messaggio al simulatore
	public void sendMessage(String topic, String message) {
		logger.log(BROKER, "Sending  message [" + message + "] to topic " +  topic);
		brokerServer.simulatorEndpoint.publish(topic, Buffer.buffer(message), MqttQoS.AT_MOST_ONCE, false,
				false);
	}

}
