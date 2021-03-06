package com.unimib.smarthome.sec;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.request.Request;
import com.unimib.smarthome.util.RequestValidator;



public class SEC {
	Logger logger = LogManager.getLogger();
	Level SEC_LEVEL = Level.getLevel("SEC");
	
	private ConcurrentLinkedQueue<Request> secQueue = new ConcurrentLinkedQueue<>();
	private ConflictSupervisor conflictSupervisor;
	private ErrorSupervisor errorSupervisor;
	private ConflictPool conflictPool;
	
	private SEC() {
		conflictSupervisor = new ConflictSupervisor();
		errorSupervisor = new ErrorSupervisor();
		conflictPool = new ConflictPool(this);
	}
	
	private static class LazyHolder{
		public static final SEC INSTANCE = new SEC();
	}
	
	public static SEC getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	//Mette una richiesta in coda per essere valutata
	public void addRequestToSECQueue(Request request) { 
		secQueue.add(request);
	}
	
	//Controlla se ci sono nuove richieste da esaminare
	protected void controlIncomingRequest() {
		Request r;
		if((r = secQueue.poll()) != null) { //Se ci sono nuove richieste
			evaluateRequest(r);
		}
	}
	
	protected void evaluateRequest(Request r) {
		logger.printf(SEC_LEVEL, "Evaluating %s", r);
		
		if(RequestValidator.controlRequestConditions(r)) {
			if(conflictSupervisor.controlRequest(r)) {
				if(errorSupervisor.executeRequest(r)) {
					
					logger.printf(SEC_LEVEL, "No errors detected on request %d", r.hashCode());
					
					conflictSupervisor.executedRequest(r);
				} 
				
				
			}else {
				logger.printf(SEC_LEVEL, "Putting the request %d into the ConflictPool", r.hashCode());
				conflictPool.addRequestToPool(r);
			}
		}else {
			logger.printf(SEC_LEVEL, "Conditions of request %d aren't satisfied", r.hashCode());  
		}
	}
	
	protected void startConflictPool() {
		conflictPool.start();
	}

	public void interruptConflictPool() {
		conflictPool.interrupt();
	}
	
	public ConflictPool getConflictPool() {
		return conflictPool;
	}
	
}
