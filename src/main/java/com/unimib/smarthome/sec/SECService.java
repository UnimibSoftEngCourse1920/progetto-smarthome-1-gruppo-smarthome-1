package com.unimib.smarthome.sec;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SECService extends Thread{

	Logger logger = LogManager.getLogger();
	SEC sec = SEC.getInstance();
	
	@Override
	public void run() {
		logger.info("Starting SEC Service");
		
		sec.startConflictPool();
		
		while(!Thread.interrupted()) {
			sec.controlIncomingRequest();
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				logger.printf(Level.WARN, "%s", e);
			}
		}
		
	}
	
	@Override
	public void interrupt() {
		sec.interruptConflictPool();
		super.interrupt();
	}
	
}
