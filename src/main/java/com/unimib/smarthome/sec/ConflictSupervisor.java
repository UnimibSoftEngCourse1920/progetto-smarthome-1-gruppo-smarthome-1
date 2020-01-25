package com.unimib.smarthome.sec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.temp.*;
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
		
		//Controllo se nelle conseguenze della richiesta ci sia qualche conflitto con le richieste retained
		LinkedList<EA> consequences = request.consequences;
			
		boolean requestCanBeExecuted = true;
		
		//Itero tutte le conseguenze
		Iterator<EA> it = consequences.iterator();
		while(requestCanBeExecuted && it.hasNext()) {
			
			//Per ogni conseguenza verifico se l'entità ha qualche conflitto con qualche richiesta
			EA ea = it.next();
			
			//Se è != da null significa che l'entità ha una lista di richieste retained con cui potrebbero esserci dei conflitti
			if(retainedRequests.get(ea.id) != null) {
				
				//Controllo tutte le richieste con cui va in conflitto
				Set<Request> conflictingRequests = retainedRequests.get(ea.id);
				Iterator<Request> crIt = conflictingRequests.iterator();
				
				while(requestCanBeExecuted && crIt.hasNext()) {
					//Richiesta con cui potrebbe andare in conflitto
					Request conflictingRequest = crIt.next();
					
					//Se la richiesta che sto provando ad eseguire ha una priorità maggiore
					if(request.priority > conflictingRequest.priority) {
						//E' inutile controllare altre condizioni, so che ha una priorità maggiore perciò viene eseguita in ogni caso
						continue;
					}else {
						//Non ha priorità maggiore perciò devo controllare se le condizioni della richiesta con cui ho il conflitto sono ancora verificate
						
						//Se tutte le condizioni di questa richiesta sono ancora rispettate
						if(RequestValidator.controlRequestConditions(conflictingRequest)) {
							
							requestCanBeExecuted = false;
							
							logger.printf(SEC, "Request %d has conflict with request %d", request.hashCode(), conflictingRequest.hashCode());
							
							//METTO LA RICHIESTA NELLA POOL
							
						}else {
							//Levo la richiesta dalla coda di richieste retained e controllo le altre richieste
							
							conflictingRequests.remove(conflictingRequest);
						}
					}
				}
					
			}
				
		}
		
		return(requestCanBeExecuted);
	}
	
	protected void addRetainedRequest(Request request) {
		LinkedList<EA> consequences = request.consequences;
		Iterator<EA> it = consequences.iterator();
		
		while(it.hasNext()) {
			EA consequence = it.next();
			
			Set<Request> retainedSet;
			if((retainedSet = retainedRequests.get(consequence.id)) == null) {
				retainedSet = new HashSet<Request>();
			}
			
			logger.printf(SEC, "Adding retained request to entity [request: %d, entity: %d]", request.hashCode(), consequence.id);
			retainedSet.add(request);
			
			retainedRequests.put(consequence.id, retainedSet);
		}
		
	}
	

}
