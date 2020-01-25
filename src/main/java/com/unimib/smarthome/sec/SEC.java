package com.unimib.smarthome.sec;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.unimib.smarthome.temp.*;



public class SEC {
	Logger logger = LogManager.getLogger();
	Level SEC = Level.getLevel("SEC");
	
	private ConcurrentLinkedQueue<Request> secQueue = new ConcurrentLinkedQueue<>();
	private ConflictSupervisor conflictSupervisor;
	private ErrorSupervisor errorSupervisor;
	private ConflictPool conflictPool;
	private static SEC instance = null;
	
	private SEC() {
		conflictSupervisor = new ConflictSupervisor();
		errorSupervisor = new ErrorSupervisor();
		conflictPool = new ConflictPool(this);
	}
	
	public static SEC getInstance() {
		if(instance == null) {
			instance = new SEC();
		}
		return instance;
	}
	
	//Mette una richiesta in coda per essere valutata
	public void evaluateRequest(Request request) {
		secQueue.add(request);
	}
	
	//Controlla se ci sono nuove richieste da esaminare
	protected void controlIncomingRequest() {
		Request r;
		if((r = secQueue.poll()) != null) { //Se ci sono nuove richieste
			
			logger.printf(SEC, "Evaluating request [id: %d]", r.hashCode());
			
			if(conflictSupervisor.controlRequest(r)) {
				boolean status = errorSupervisor.executeRequest(r);
				
				if(status) {
					//Se la richiesta è andata a buon fine, controllo se era ti tipo retain e avviso il supervisore dei conflitti
					
					logger.printf(SEC, "No conflicts detected on request %d", r.hashCode());
					
				} //Se la richiesta ha lanciato un errore il supervisore dei conflitti avra gia gestito la situazione
				
				if(r.retain) {
					conflictSupervisor.addRetainedRequest(r);
				}
				
			}else {
				logger.printf(SEC, "Putting request %d in the ConflictPool", r.hashCode());
				conflictPool.addRequestToPool(r);
			}
		}
	}
	
	protected void startConflictPool() {
		conflictPool.start();
	}

	public void interruptConflictPool() {
		conflictPool.interrupt();
	}
	
	
}
