package com.unimib.smarthome.sec;


import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.unimib.smarthome.request.Request;


public class ConflictPool extends Thread{
	Logger logger = LogManager.getLogger();
	Level SEC_LEVEL = Level.getLevel("SEC");

	private SEC sec = null;
	private ConcurrentLinkedQueue<Request> conflictPoolQueue = new ConcurrentLinkedQueue<>();
	
	protected ConflictPool(SEC sec) {
		this.sec = sec;
	}
	
	@Override
	public void run() {
		
		logger.log(SEC_LEVEL, "Starting ConflictPool");
		
		while(!Thread.interrupted()) {
			
			Request[] cpCopy = getConflictPool();
			
			for(Request request : cpCopy) {
				logger.printf(SEC_LEVEL, "Trying to evaluate request from conflict pool [id: %d]", request.hashCode());
				conflictPoolQueue.remove(request);
				sec.evaluateRequest(request);
			}
			
			try {
				Thread.sleep(5000); //10s
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	protected void addRequestToPool(Request r) {
		if(!conflictPoolQueue.contains(r)) {
			logger.printf(SEC_LEVEL, "Added request into the conflict pool [id: %d]", r.hashCode());
			conflictPoolQueue.add(r);
		}
	}
	
	public int countRequestOnPool() {
		return conflictPoolQueue.size();
	}
	
	public void clearPool() {
		conflictPoolQueue.clear();
	}
	
	public Request[] getConflictPool(){
		if(conflictPoolQueue != null)
			return conflictPoolQueue.toArray(new Request[0]); 
		return new Request[0];
		
	}

}
