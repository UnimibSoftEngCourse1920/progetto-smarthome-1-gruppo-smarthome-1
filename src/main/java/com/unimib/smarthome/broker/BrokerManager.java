package com.unimib.smarthome.broker;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.entity.exceptions.MissingEntityTopicException;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.messages.MqttPublishMessage;

public class BrokerManager {

	private Logger logger = LogManager.getLogger();
	final static Level BROKER = Level.forName("BROKER", 350);
	
	static BrokerManager instance;
	private BrokerServer brokerServer;
	private EntityManager entityManager;

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

	public void dispatchMessage(MqttPublishMessage message) {
		try {
			entityManager.handleBrokerMessage(message.topicName(), message.payload().toString());
		} catch (MissingEntityTopicException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String topic, String message) {

		brokerServer.simulatorEndpoint.publish(topic, Buffer.buffer(message), MqttQoS.AT_MOST_ONCE, false,
				false);
	}

}
