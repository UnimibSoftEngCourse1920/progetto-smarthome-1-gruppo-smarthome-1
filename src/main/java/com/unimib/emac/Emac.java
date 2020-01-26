package com.unimib.emac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unimib.common.Observer;
import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.request.Request;


public class Emac implements Observer {

	static Emac instance;
	
	private Map<Integer, List<Request>> idToRequests = new HashMap<>();
	
	//Singleton
	public static Emac getInstance() {
		if(instance == null) {				
			instance = new Emac();
			}
		return instance;
	}
	
	public void registerAutomation(Request r) {
		
		for (int i = 0; i < r.getCondition().length; i++) {
			//id di un'entità tra le condition di r
			int entityId = r.getCondition()[i].getEntityID();
			//lista di richieste già salvate relative a quell'entità
			List<Request> requests = idToRequests.get(entityId);
			//aggiungo r alla lista
			requests.add(r);
			//aggiungo la lista aggiornata alla mappa
			idToRequests.put(entityId, requests);
		}
	}
	
	

	
	public void evaluate(EntityManager e) {

	}

}
