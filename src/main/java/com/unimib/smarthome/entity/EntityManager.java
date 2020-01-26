package com.unimib.smarthome.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.common.Observer;
import com.unimib.common.Subject;
import com.unimib.smarthome.broker.BrokerManager;
import com.unimib.smarthome.entity.exceptions.DuplicatedEntityException;
import com.unimib.smarthome.entity.exceptions.EntityIncomingMessageException;

/**
 * Classe usata per la gestione di tutte le entita
 */

public class EntityManager implements Subject {

	static EntityManager instance;
	private BrokerManager brokerManager = BrokerManager.getInstance();
	private Map<Integer, Entity> entityList = new HashMap<>();
	protected ArrayList<Observer> observers;

	private Logger logger = LogManager.getLogger();
	final Level EM = Level.getLevel("EM");
	
	
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
		
		if(entityList.containsKey(entityID)) //Controllo che non ci siano entita con lo stesso id
			throw new DuplicatedEntityException(entity, entityList.get(entityID));
		
		logger.printf(EM, "Registered entity [id: %d, name: %s]", entityID, entity.getName());
		entityList.put(entityID, entity);
		
	}
	
	
	//Inoltra un messaggio ad una entita
	public void sendEntityMessage(int entityID, String message) throws EntityIncomingMessageException {
		logger.printf(EM, "Sending message to entity [id: %d, message: %s]", entityID, message);
		entityList.get(entityID).onIncomingMessage(message);
	}
	
	public void notifyEntityChange(Entity entity) {		
		
		//NOTIFICA OSSERVATORI
		this.notifyObservers(entity);
		
		//Aggiorno l'entita nella lista
		entityList.put(entity.getId(), entity);
		
		//Se sono le entita del simulatore notifico attraverso il server MQTT
		if(entity instanceof SimulatorEntity) {
			SimulatorEntity se = (SimulatorEntity) entity;
			brokerManager.sendMessageToClient(se.getTopic(), String.valueOf(se.getState()));
		}
			
	}
	
	public String getEntityState(int entityID) {
		return entityList.get(entityID).getState();
	}
	
	public Map<Integer, Entity> getEntityMap(){
		return Map.copyOf(entityList);
	}
	
	/** OBSERVER PATTERN **/
	


	public void attach(Observer o) {
		observers.add(o);
	}
	
	public boolean detach(Observer o) {
		return observers.remove(o);
	}

	@Override
	public void notifyObservers(Entity entity) {
		for (Observer o : observers) {
			o.update(entity.getId(), entity.getState());
		}
	}

}
