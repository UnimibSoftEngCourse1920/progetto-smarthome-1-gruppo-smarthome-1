package com.unimib.smarthome.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.broker.BrokerManager;
import com.unimib.smarthome.common.Observer;
import com.unimib.smarthome.common.Subject;
import com.unimib.smarthome.entity.exceptions.DuplicatedEntityException;
import com.unimib.smarthome.entity.exceptions.EntityIncomingMessageException;

/**
 * Classe usata per la gestione di tutte le entita
 */

public class EntityManager implements Subject {

	public static EntityManager instance;
	private static BrokerManager brokerManager = BrokerManager.getInstance();
	private static ConcurrentMap<Integer, Entity> entityMap = new ConcurrentHashMap<>();
	protected List<Observer> observers = new ArrayList<>();

	private Logger logger = LogManager.getLogger();
	final Level EM = Level.getLevel("EM");
	
	private EntityManager() {}
	
	private static class LazyHolder {
        private static final EntityManager instance = new EntityManager();
    }

    public static EntityManager getInstance() {
        return LazyHolder.instance;
    }
	
	//Registra una nuova entita
	public void registerEntity(Entity entity) throws DuplicatedEntityException {		
		int entityID = entity.getID();
	
		if(entityMap.containsKey(entityID)) //Controllo che non ci siano entita con lo stesso id

			throw new DuplicatedEntityException(entity, entityMap.get(entityID));
		
		logger.printf(EM, "Registered entity [id: %d, name: %s]", entityID, entity.getName());
		entityMap.put(entityID, entity);
		
		if(entity instanceof SimulatorEntity) {
			brokerManager.registryEntityTopic(entityID, ((SimulatorEntity) entity).getTopic());
		}
	}
	
	//Inoltra un messaggio ad una entita
	public void sendEntityMessage(int entityID, String message) throws EntityIncomingMessageException {
		logger.printf(EM, "Sending message to entity [id: %d, message: %s]", entityID, message);
		Entity entity = entityMap.get(entityID);
		try{
			entity.onIncomingMessage(message, Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()));
		}catch(EntityIncomingMessageException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			entity.onIncomingMessage(message, this.getClass());
			e.printStackTrace();
		}
		//Se non lancio un errore
		notifyEntityChange(entity);
			
	}
	
	public void notifyEntityChange(Entity entity) {		
		
		//NOTIFICA OSSERVATORI
		notifyObservers(entity);
		
		//Aggiorno l'entita nella lista
		entityMap.put(entity.getID(), entity);
		
		//Se sono le entita del simulatore notifico attraverso il server MQTT
		if(entity instanceof SimulatorEntity) {
			SimulatorEntity se = (SimulatorEntity) entity;
			brokerManager.enqueueMessageToSimulator(se.getTopic(), String.valueOf(se.getState()));
		}
			
	}
	
	public String getEntityState(int entityID) {
		return entityMap.get(entityID).getState();
	}
	
	public Map<Integer, Entity> getEntityMap(){
		return Map.copyOf(entityMap);
	}
	
	/** OBSERVER PATTERN **/
	
	public void attach(Observer o) {
		logger.printf(EM, "Aggiunto osservatore %s", o);
		observers.add(o);
	}
	
	public boolean detach(Observer o) {
		return observers.remove(o);
	}

	@Override
	public void notifyObservers(Entity entity) {
		for (Observer o : observers) {
			o.update(entity);
		}
	}
}
