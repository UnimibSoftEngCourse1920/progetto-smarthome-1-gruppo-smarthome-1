package com.unimib.smarthome.broker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.entity.exceptions.EntityIncomingMessageException;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.messages.MqttPublishMessage;

public class BrokerManager {

	private Logger logger = LogManager.getLogger();
	final static Level BROKER_LEVEL = Level.getLevel("BROKER");
	
	private BrokerServer brokerServer;
	private EntityManager entityManager = EntityManager.getInstance();

	private static ConcurrentMap<String, Integer> brokerMap = new ConcurrentHashMap<>();
	private ConcurrentLinkedQueue<EntityStatus> simulatorMessageQueue = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<MqttPublishMessage> entityMessageQueue = new ConcurrentLinkedQueue<>();
	
	class EntityStatus{
		public EntityStatus(String topic, String message) {
			this.topic = topic;
			this.message = message;
		}
		String topic;
		String message;
	}
	
	private BrokerManager() {}
	
	private static class LazyHolder {
        private static final BrokerManager instance = new BrokerManager();
    }

    public static BrokerManager getInstance() {
        return LazyHolder.instance;
    }


	public void startBrokerServer() {
		try {
			brokerServer = new BrokerServer();
			brokerServer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void registryEntityTopic(int entityID, String topic){
		if(!brokerMap.containsKey(topic)) {
			logger.log(BROKER_LEVEL, "Associated topic [" + topic + "] to entity [" + entityID + "]");
			brokerMap.put(topic, entityID);
		}else {
			logger.log(BROKER_LEVEL, "Topic %s has yet an entity associated ", topic);
		}	
	}
	
	public void sendMessageToEntity() {
		MqttPublishMessage message;
		if((message = entityMessageQueue.poll()) != null ) {
			logger.printf(BROKER_LEVEL, message.topicName());
			if(brokerMap.containsKey(message.topicName())) {
				
				int entityID = brokerMap.get(message.topicName());
				logger.printf(BROKER_LEVEL, "Dispaching message [%s] to entity %d", message.payload(), entityID);
				try {
					entityManager.sendEntityMessage(entityID, message.payload().toString());
				} catch (EntityIncomingMessageException e) {
					e.printStackTrace();
				}
			}else {
				logger.printf(BROKER_LEVEL, "Incoming message from topic %s but there aren't any entity associated with that.", message.topicName());
			}
			
		}
	}

	protected void sendMessageToSimulator() {
		if(brokerServer.simulatorEndpoint != null) { //Se c'e un simulatore collegato :)
			EntityStatus es;
			if((es = simulatorMessageQueue.poll()) != null ) {
				logger.printf(BROKER_LEVEL, "Sending  message [%s] to topic %s", es.message, es.topic);
				brokerServer.simulatorEndpoint.publish(es.topic, Buffer.buffer(es.message), MqttQoS.AT_MOST_ONCE, false,
						false);
			}
		}
	}
	
	
	//Chiamato quando ricevo un messaggio dal simulatore
	public void enqueueMessageToEntity(MqttPublishMessage message) {		
		entityMessageQueue.add(message);
	}

	//Chiamato per mandare un messaggio al simulatore
	public void enqueueMessageToSimulator(String topic, String message) {
		simulatorMessageQueue.add(new EntityStatus(topic, message));
	}

}
