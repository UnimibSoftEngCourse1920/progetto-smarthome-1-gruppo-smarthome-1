package com.unimib.smarthome.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.unimib.common.Observer;
import com.unimib.common.Subject;
import com.unimib.smarthome.broker.BrokerManager;
import com.unimib.smarthome.entity.exceptions.DuplicatedEntityException;

/**
 * Classe usata per la gestione di tutte le entita
 */

public class EntityManager implements Subject {

	static EntityManager instance;
	private BrokerManager brokerManager = BrokerManager.getInstance();
	private Map<Integer, Entity> entityList = new HashMap<>();
	protected ArrayList<Observer> observers;

	
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
		
	}
	
	
	//Prende tutti i nuovi messaggi per le enita e gli fa gestire il nuovo messaggio in ingresso
	public void updateEntity(int entityID, String message) {
		entityList.get(entityID).onIncomingMessage(message);
	}
	
	public void notifyEntityChange(Entity entity) {		
		
		//NOTIFICA OSSERVATORI
		this.notifyAddAll();
		
		//Aggiorno l'entita nella lista
		entityList.put(entity.getId(), entity);
		
		//Se sono le entita del simulatore notifico attraverso il server MQTT
		if(entity instanceof SimulatorEntity) {
			SimulatorEntity se = (SimulatorEntity) entity;
			brokerManager.sendMessage(se.getTopic(), String.valueOf(se.getState()));
		}
			
	}
	
	public void attach(Observer o) {
		observers.add(o);
		this.notifyAdd(o);
	}
	public boolean detach(Observer o) {
		boolean a = observers.remove(o);
		o.updateRemove(this);
		return a;
	}

	public void notifyAdd(Observer o) {
		o.updateAdd(this);
	}

	public void notifyAddAll() {
		for (Observer o : observers) {
			o.updateAdd(this);
		}
	}

	public void notifyRemove(Observer o) {
		o.updateRemove(this);
	}

	public void notifyRemoveAll() {
		for (Observer o : observers) {
			o.updateRemove(this);
		}
	}
	
	
}
