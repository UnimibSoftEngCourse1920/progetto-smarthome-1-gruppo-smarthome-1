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

public class ConflictSupervisor {
	Logger logger = LogManager.getLogger();
	Level SEC_LEVEL = Level.getLevel("SEC");
	private Map<Integer, Set<Request>> retainedRequests = new HashMap<>();
	
	/**
	 * 
	 * @param request richiesta da controllare
	 * @return true, se puo essere eseguita senza generare conflitti, false altrimenti. 
	 */
	protected boolean controlRequest(Request request) {
		
		EntityStatus[] consequences = request.getConsequences();
		var wrapper = new Object(){ 
			boolean requestCanBeExecuted;
			};
		wrapper.requestCanBeExecuted = true;

		for(EntityStatus ea : consequences){
			if(retainedRequests.get(ea.getEntityID()) != null) {
				
				Set<Request> conflictingRequests = retainedRequests.get(ea.getEntityID());
				
				conflictingRequests.forEach(conflictingRequest -> {
					
					if(request.getPriority() > conflictingRequest.getPriority()) {
						
						logger.printf(SEC_LEVEL, "Retained request with lower priority found. Removing it from memory [request: %d, lowerRequest: %d]", request.hashCode(), conflictingRequest.hashCode());
						removeRetainRequest(conflictingRequest);
						
					}else if(RequestValidator.controlRequestConditions(conflictingRequest)) {  //Se tutte le condizioni di questa richiesta sono ancora rispettate
						
						wrapper.requestCanBeExecuted = false;
						logger.printf(SEC_LEVEL, "Request %d has conflict(s) with request %d", request.hashCode(), conflictingRequest.hashCode());
						
					}else {
						
						logger.printf(SEC_LEVEL, "Retained request with condition no longer respected found. Removing it from memory [request: %d]", conflictingRequest.hashCode());
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
				retainedSet = new HashSet<>();
			}
			
			logger.printf(SEC_LEVEL, "Adding retained request to entity [request: %d, entity: %d]", request.hashCode(), consequence.getEntityID());
			retainedSet.add(request);
			
			retainedRequests.put(consequence.getEntityID(), retainedSet);
		}
	}
	
	/*Metodo chiamato per informare il ConflictSupervisor che una richiesta è stata eseguita senza problemi*/
	protected void executedRequest(Request request) {
		if(request.getRetain()) {
			addRetainedRequest(request);
		}
	}
	
	protected void removeRetainRequest(Request request) {
		
		retainedRequests.forEach((entityID, retainedSet) -> {
			if(retainedSet.contains(request)) {
				logger.printf(SEC_LEVEL, "Removing retained request to entity [request: %d, entity: %d]", request.hashCode(), entityID);
				retainedSet.remove(request);
			}
		});
		
	}
	

}
