package com.unimib.smarthome.sec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.request.EntityStatus;
import com.unimib.smarthome.request.Request;
import com.unimib.smarthome.util.RequestValidator;

/*
 * ESEMPIO DI CONFLITTO:
 * 	Request {
 * 			if: [GAS > 20, persone = 0]
 * 			then: [finestra = Aperta, allarme = disattivato]
 * 			retain: true
 * 			retain_level: 5
 * }
 * 	Request {
 * 			if: [Temperatura < 25, Finestra = Aperta]
 * 			then: [finestra = Chiusa]
 * }
 * 
 */

public class ConflictSupervisor {
	Logger logger = LogManager.getLogger();
	Level SEC = Level.getLevel("SEC");
	private Map<Integer, Set<Request>> retainedRequests = new HashMap<>();
	
	/**
	 * 
	 * @param request richiesta da controllare
	 * @return true, se puo essere eseguita senza generare conflitti, false altrimenti. 
	 */
	protected boolean controlRequest(Request request) {
		
		EntityStatus[] consequences = request.getConsequences();
		var wrapper = new Object(){ boolean requestCanBeExecuted; };
		

		for(EntityStatus ea : consequences){
			if(retainedRequests.get(ea.getEntityID()) != null) {
				
				Set<Request> conflictingRequests = retainedRequests.get(ea.getEntityID());
				
				conflictingRequests.forEach((conflictingRequest) -> {
					
					if(request.getPriority() > conflictingRequest.getPriority()) {
						
						logger.printf(SEC, "Found retained request with lower priority. Removing it from memory [request: %d, priority: %d, lowerRequest: %d, lowerRequestPriority: %d]", request.hashCode(), request.getPriority(), conflictingRequest.hashCode(), conflictingRequest.getPriority());
						removeRetainRequest(conflictingRequest);
						
					}else if(RequestValidator.controlRequestConditions(conflictingRequest)) {  //Se tutte le condizioni di questa richiesta sono ancora rispettate
						
						wrapper.requestCanBeExecuted = false;
						logger.printf(SEC, "Request %d has conflict with request %d", request.hashCode(), conflictingRequest.hashCode());
						
					}else {
						
						logger.printf(SEC, "Found retained request with condition no longer respected. Removing it from memory [request: %d]", conflictingRequest.hashCode());
						removeRetainRequest(conflictingRequest);
						
					}
					
					
				});
						
			}
				
		}
		
		return(wrapper.requestCanBeExecuted);
	}
	
	protected void addRetainedRequest(Request request) {
		EntityStatus[] consequences = request.getConsequences();
		
		for(EntityStatus consequence : consequences) {
			Set<Request> retainedSet;
			if((retainedSet = retainedRequests.get(consequence.getEntityID())) == null) {
				retainedSet = new HashSet<Request>();
			}
			
			logger.printf(SEC, "Adding retained request to entity [request: %d, entity: %d]", request.hashCode(), consequence.getEntityID());
			retainedSet.add(request);
			
			retainedRequests.put(consequence.getEntityID(), retainedSet);
		}
		
	}
	
	protected void removeRetainRequest(Request request) {
		
		retainedRequests.forEach((entityID, retainedSet) -> {
			if(retainedSet.contains(request)) {
				logger.printf(SEC, "Removing retained request to entity [request: %d, entity: %d]", request.hashCode(), entityID);
			}
		});
		
	}
	

}
