package com.unimib.smarthome.monitor;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.common.Observer;
import com.unimib.smarthome.entity.Entity;

public class MonitorService extends Thread implements Observer  {
	private Logger logger = LogManager.getLogger();
	private final Level MONITOR_LEVEL = Level.getLevel("MONITOR");
	private ConcurrentLinkedQueue<Entity> monitorQueue = new ConcurrentLinkedQueue<>();
	Entity lastInfo = null;


	@Override
	public void run() {
		logger.info("Starting monitor service");
		while(!Thread.interrupted()) {
			
			if((lastInfo = monitorQueue.poll()) != null ) {
					logger.printf(MONITOR_LEVEL, "Sensor id %i named %s has a new state: %s", lastInfo.getId(), lastInfo.getName(), lastInfo.getState());
			}
			
			//DO OTHER ANALISYS STUFF...
			
		}
	}
	
	//Nel metodo update di observer aggiungere il messaggio alla coda
	
}
