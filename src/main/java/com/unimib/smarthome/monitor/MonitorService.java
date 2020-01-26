package com.unimib.smarthome.monitor;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.common.Observer;

class EntityInfo{
	public int id;
	public String state;
	public String name;
}

public class MonitorService extends Thread implements Observer  {
	private Logger logger = LogManager.getLogger();
	private final Level MONITOR = Level.getLevel("MONITOR");
	private ConcurrentLinkedQueue<EntityInfo> monitorQueue = new ConcurrentLinkedQueue<>();
	EntityInfo lastInfo = null;


	@Override
	public void run() {
		logger.info("Starting monitor service");
		while(!Thread.interrupted()) {
			
			if((lastInfo = monitorQueue.poll()) != null ) {
					logger.printf(MONITOR, "Sensor id %i named %s has a new state: %s", lastInfo.id, lastInfo.name, lastInfo.state);
			}
			
			//DO OTHER ANALISYS STUFF...
			
		}
	}
	
	//Nel metodo update di observer aggiungere il messaggio alla coda
	
}
