package com.unimib.smarthome.sec;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.console.CLIEvaluation;
import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.entity.exceptions.EntityIncomingMessageException;
import com.unimib.smarthome.request.EntityStatus;
import com.unimib.smarthome.request.Request;

/*
 * Una richiesta genera errore se gli imposto ad una entita un valore "invalido"
 * 
 */
public class ErrorSupervisor {
	
	Logger logger = LogManager.getLogger();
	Level SEC_LEVEL = Level.getLevel("SEC");
	private EntityManager entityManager = EntityManager.getInstance();
	
	public boolean executeRequest(Request r) {	
		
		if(r.getAskPermission()){ //se deve chiedere il permesso
			
			CLIEvaluation.askPermission(r);
			return false;
		}
		
		return sendMessagesToEntities(r.getConsequences());
		
	}
	 
	private boolean sendMessagesToEntities(EntityStatus[] consequences) {
		boolean noError = true;
		
		for(EntityStatus es : consequences) {
			try {
				entityManager.sendEntityMessage(es.getEntityID(), es.getState());
			} catch (EntityIncomingMessageException e) {
				logger.log(SEC_LEVEL, e.getCause());
				noError = true;
				//PRENDE DECISIONE IN AUTOMATICO
			}
		}
		
		return noError;
	}
	
	
}
