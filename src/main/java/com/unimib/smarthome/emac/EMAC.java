package com.unimib.smarthome.emac;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.common.Observer;
import com.unimib.smarthome.entity.Entity;
import com.unimib.smarthome.request.Request;
import com.unimib.smarthome.sec.SEC;
import com.unimib.smarthome.util.RequestValidator;


public class EMAC implements Observer {

	private Map<Integer, List<Request>> idToRequests = new HashMap<>();
	private ConcurrentLinkedQueue<Entity> statusUpdateQueue = new ConcurrentLinkedQueue<>();
	private Logger logger = LogManager.getLogger();
	private final Level EMAC_LEVEL = Level.getLevel("EMAC");
	private final SEC sec = SEC.getInstance();
	private static EMAC instance;
	
	private EMAC() {}
	
	public static EMAC getInstance() {
		if(instance == null)
			instance = new EMAC();
		return instance;
	}
	
	
	public void registerAutomation(Request r) {
		for (int i = 0; i < r.getConditions().length; i++) {
			//id di un'entite'  tra le condition di r
			int entityId = r.getConditions()[i].getEntityID();
			//lista di richieste gia'  salvate relative a quell' entita' 
			List<Request> requests = idToRequests.get(entityId);
			//aggiungo r alla lista
			requests.add(r);
			//aggiungo la lista aggiornata alla mappa
			idToRequests.put(entityId, requests);
		}
	}
	

	private List<Request> filter(int entityId) {
		if(idToRequests.containsKey(entityId)) { //Se contiene richieste associate all'entita'
			List<Request> requests = idToRequests.get(entityId);
			List<Request> validRequests = new ArrayList<>();
			
			for (Request r : requests) {
				if(RequestValidator.controlRequestConditions(r))
					validRequests.add(r);
				}
			return validRequests;
		}
		
		return null; //Non contiene richieste associate all'entita'
	}
	
	
	
	private void execute(int entityId) {
		List<Request> validRequests = filter(entityId);
		if(validRequests != null) {
			logger.printf(EMAC_LEVEL, "New state found on entity %d. %d valid automations found", entityId, validRequests.size());
			Collections.sort(validRequests);
			Collections.reverse(validRequests);
			for (Request r : validRequests) { //Prima esegue le retain 
				if (r.getRetain()) {
					logger.printf(EMAC_LEVEL, "Executing request %d", r.hashCode());
					sec.addRequestToSECQueue(r);
					validRequests.remove(r);
				}
			}
			for (Request r : validRequests) { //Dopo esegue le normali
				logger.printf(EMAC_LEVEL, "Executing request %d", r.hashCode());
				sec.addRequestToSECQueue(r);
			}	
		}
	}

	@Override
	public void update(Entity entity) {
		statusUpdateQueue.add(entity);
	}
	
	protected void controlNewStatus() {
		if(!statusUpdateQueue.isEmpty()) {
			this.execute(statusUpdateQueue.poll().getID());
		}
	}
}
