package com.unimib.smarthome.broker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.entity.SimulatorEntity;
import com.unimib.smarthome.entity.exceptions.EntityIncomingMessageException;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.messages.MqttPublishMessage;

public class BrokerManager {

	private Logger logger = LogManager.getLogger();
	final Level BROKER_LEVEL = Level.getLevel("BROKER");
	
	private BrokerServer brokerServer;

	private ConcurrentMap<String, Integer> brokerMap = new ConcurrentHashMap<>(); //static
	private ConcurrentLinkedQueue<SimulatorEntity> simulatorMessageQueue = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<MqttPublishMessage> entityMessageQueue = new ConcurrentLinkedQueue<>();
	

	private BrokerManager() {}
	
	private static class LazyHolder {
        private static final BrokerManager INSTANCE = new BrokerManager();
    }

    public static BrokerManager getInstance() {
        return LazyHolder.INSTANCE;
    }


	public void startBrokerServer() {
		try {
			brokerServer = new BrokerServer();
			brokerServer.start();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	public void registerEntityTopic(int entityID, String topic){
		if(!brokerMap.containsKey(topic)) {
			logger.printf(BROKER_LEVEL, "Associated topic [%s] to entity [%d]", topic, entityID);
			brokerMap.put(topic, entityID);
		}else {
			logger.log(BROKER_LEVEL, "Topic %s is already associated with an entity ", topic);
		}	
	}
	
	public void sendMessageToEntity() {
		MqttPublishMessage message;
		if((message = entityMessageQueue.poll()) != null ) {
			if(brokerMap.containsKey(message.topicName())) {
				
				int entityID = brokerMap.get(message.topicName());
				logger.printf(BROKER_LEVEL, "Sending message to EntityManager [entity: %d, message: %s]", entityID, message.payload());

					try {
						EntityManager.getInstance().sendEntityMessage(entityID, message.payload().toString());
					} catch (EntityIncomingMessageException e) {
						logger.error(e.getLocalizedMessage());
					}

			}else {
				logger.printf(BROKER_LEVEL, "Incoming message from topic %s but there are no entities associated with it.", message.topicName());
			}
			
		}
	}

	protected void sendMessageToSimulator() {
		if(brokerServer.getSimulatorEndpoint() != null) { //Se c'e un simulatore collegato :)
			SimulatorEntity es;
			if((es = simulatorMessageQueue.poll()) != null ) {
				logger.printf(BROKER_LEVEL, "Sending  message to simulator [message: %s, topic: %s]", es.getState(), es.getTopic());
				brokerServer.getSimulatorEndpoint().publish(es.getTopic(), Buffer.buffer(es.getState()), MqttQoS.AT_MOST_ONCE, false,
						false);
			}
		}
	}
	
	public void enqueueMessageToEntity(MqttPublishMessage message) { //Chiamato per inoltrare un messaggio dal simulatore a EntityManager
		entityMessageQueue.add(message);
	}

	
	public void enqueueEntityToSimulator(SimulatorEntity entity) { //Chiamato per aggiornare una entita' sul simulatore
		simulatorMessageQueue.add(entity);
	}
	
}
