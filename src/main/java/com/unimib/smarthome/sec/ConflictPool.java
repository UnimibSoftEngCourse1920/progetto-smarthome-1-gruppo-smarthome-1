package com.unimib.smarthome.sec;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.temp.Request;

public class ConflictPool extends Thread{
	Logger logger = LogManager.getLogger();
	Level SECL = Level.getLevel("SEC");

	private SEC sec = null;
	private ConcurrentLinkedQueue<Request> conflictPool = new ConcurrentLinkedQueue<>();
	
	public ConflictPool(SEC sec) {
		this.sec = sec;
	}
	
	@Override
	public void run() {
		
		logger.printf(SECL, "Starting ConflictPool");
		
		while(!Thread.interrupted()) {
			conflictPool.forEach((request) -> {
				logger.printf(SECL, "Trying to evaluate request from conflict pool [id: %d]", request.hashCode());
				sec.evaluateRequest(request);
			});
			try {
				Thread.sleep(10000); //10s
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	public void addRequestToPool(Request r) {
		if(!conflictPool.contains(r))
			conflictPool.add(r);
	}
	


}
