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

	private BrokerManager brokerManager = BrokerManager.getInstance(); //static
	private ConcurrentMap<Integer, Entity> entityMap = new ConcurrentHashMap<>(); //static
	protected List<Observer> observers = new ArrayList<>();

	private Logger logger = LogManager.getLogger();
	final Level EM = Level.getLevel("EM");
	
	private EntityManager() {}
	
	private static class LazyHolder {
        private static final EntityManager INSTANCE = new EntityManager();
    }

    public static EntityManager getInstance() {
        return LazyHolder.INSTANCE;
    }
	
	//Registra una nuova entita
	public void registerEntity(Entity entity) throws DuplicatedEntityException {		
		int entityID = entity.getID();
	
		if(entityMap.containsKey(entityID)) //Controllo che non ci siano entita con lo stesso id

			throw new DuplicatedEntityException(entity, entityMap.get(entityID));
		
		logger.printf(EM, "Registered entity [id: %d, name: %s]", entityID, entity.getName());
		entityMap.put(entityID, entity);
		
		if(entity instanceof SimulatorEntity) {
			brokerManager.registerEntityTopic(entityID, ((SimulatorEntity) entity).getTopic());
		}
	}
	
	//Inoltra un messaggio ad una entita
	public void sendEntityMessage(int entityID, String message) throws EntityIncomingMessageException {
		logger.printf(EM, "Sending message to entity [id: %d, message: %s]", entityID, message);
		Entity oldEntity = entityMap.get(entityID);
		Entity newEntity = oldEntity;
		try{
			newEntity = oldEntity.onIncomingMessage(message, Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()));
		}catch(Exception e) {
			e.printStackTrace();
		}
//		catch(EntityIncomingMessageException e) {
//			throw e;
//		} catch (ClassNotFoundException e) {
//			newEntity = oldEntity.onIncomingMessage(message, this.getClass());
//		}
		
		logger.printf(EM, "Replacing %s -> %s inside the entityMap]", oldEntity, newEntity);
		entityMap.put(entityID, newEntity); //Aggiorno l'entita nella mappa
		
		notifyEntityChange(newEntity); //Notitifo a tutti il cambiamento
			
	}
	
	public void notifyEntityChange(Entity entity) {		
		notifyObservers(entity); //NOTIFICA OSSERVATORI		

		if(entity instanceof SimulatorEntity) { //Se sono le entita del simulatore notifico attraverso il server MQTT
			SimulatorEntity se = (SimulatorEntity) entity;
			brokerManager.enqueueEntityToSimulator(se);
		}
			
	}
	
	public String getEntityState(int entityID) {
		return entityMap.get(entityID).getState();
	}
	
	public Entity getEntity(int entityID) {
		return entityMap.get(entityID);
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
