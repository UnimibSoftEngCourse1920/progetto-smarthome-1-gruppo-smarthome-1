package com.unimib.smarthome.sec;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.entity.exceptions.EntityIncomingMessageException;
import com.unimib.smarthome.temp.EA;
import com.unimib.smarthome.temp.Request;

/*
 * Una richiesta genera errore se gli imposto ad una entita un valore "invalido"
 * 
 */



public class ErrorSupervisor {
	
	Logger logger = LogManager.getLogger();
	Level SEC = Level.getLevel("SEC");

	private EntityManager entityManager = EntityManager.getInstance();
	
	public boolean executeRequest(Request r) {	
		boolean error = false;
		
		for(EA ea : r.consequences) {
			try {
				entityManager.sendEntityMessage(ea.id, ea.state);
			} catch (EntityIncomingMessageException e) {
				logger.printf(SEC, e.getMessage());
				error = true;
				//PRENDE DECISIONE IN AUTOMATICO
			}
		}
		
		return error;
	}
	
	
}
