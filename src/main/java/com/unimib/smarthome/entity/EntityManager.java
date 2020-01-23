package com.unimib.smarthome.entity;

import java.util.HashMap;
import java.util.Map;

import com.unimib.smarthome.broker.BrokerManager;
import com.unimib.smarthome.entity.exceptions.DuplicatedEntityException;
import com.unimib.smarthome.entity.exceptions.MissingEntityTopicException;

/**
 * Classe usata per la gestione di tutte le entita
 */

public class EntityManager {

	static EntityManager instance;
	private Map<Integer, Entity> entityList = new HashMap<>();
	private Map<String, Integer> brokerMap = new HashMap<>();
	private BrokerManager messageDispatcher = BrokerManager.getInstance();
	
	private EntityManager() {}
	
	public static EntityManager getInstance() {
		if(instance == null) {
			instance = new EntityManager();
		}
		return instance;
	}
	
	//Registra una nuova entita
	public void registerEntity(Entity entity) throws DuplicatedEntityException {		
		
		int entityID = entity.getId();
		
		//Controllo che non ci siano entita con lo stesso id
		if(entityList.containsKey(entityID))
			throw new DuplicatedEntityException(entity, entityList.get(entityID));
		
		entityList.put(entityID, entity);
		
		
		//Registro le entita nella mappa del broker
		if(entity instanceof SimulatorEntity) {
			SimulatorEntity sEntity = (SimulatorEntity) entity;
			brokerMap.put(sEntity.getTopic(), entityID);
		}
	}
	
	
	//Prende tutti i nuovi messaggi per le enita e gli fa gestire il nuovo messaggio in ingresso
	public void handleNewStateMessage(Entity e, String message) {
		e.onIncomingMessage(message);
	}
	
	//Prende tutti i messaggi dal simulatore e li adatta al metodo handleNewStateMessage
	public void handleBrokerMessage(String topic, String message) throws MissingEntityTopicException {
		int entityID = brokerMap.get(topic);
		Entity entityTarget = entityList.get(entityID);
		if(entityTarget != null) {
			handleNewStateMessage(entityTarget, message);
		}else {
			throw new MissingEntityTopicException(topic);
		}
		
	}
	
	public void sendNewStateMessage(Entity entity) {
		
		//Se sono le entita del simulatore mando i nuovi messaggi attraverso il server MQTT
		if(entity instanceof SimulatorEntity) {
			SimulatorEntity se = (SimulatorEntity) entity;
			messageDispatcher.sendMessage(se.getTopic(), String.valueOf(se.getState()));
		}
			
	}
	
	
}
