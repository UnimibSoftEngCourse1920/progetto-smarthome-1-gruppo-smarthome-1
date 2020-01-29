package com.unimib.smarthome.emac;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.unimib.smarthome.entity.EntityManager;

public class EMACService extends Thread{

	EMAC emac = EMAC.getInstance();
	private Logger logger = LogManager.getLogger();
	
	@Override
	public void run() {
		logger.info("Starting EMAC service");
		EntityManager.getInstance().attach(emac);
		while(!Thread.interrupted()) {
			emac.controlNewStatus();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
}
