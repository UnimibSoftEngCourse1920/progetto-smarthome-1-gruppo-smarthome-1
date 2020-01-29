package com.unimib.smarthome.monitor;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.common.Observer;
import com.unimib.smarthome.entity.Entity;
import com.unimib.smarthome.entity.EntityManager;

public class MonitorService extends Thread implements Observer  {
	private Logger logger = LogManager.getLogger();
	private final Level MONITOR_LEVEL = Level.getLevel("MONITOR");
	private ConcurrentLinkedQueue<Entity> monitorQueue = new ConcurrentLinkedQueue<>();
	Entity lastUpdate = null;


	@Override
	public void run() {
		logger.info("Starting monitor service");
		
		EntityManager.getInstance().attach(this);
		
		while(!Thread.interrupted()) {
			
			if((lastUpdate = monitorQueue.poll()) != null ) {
					logger.printf(MONITOR_LEVEL, "Sensor id %d (%s) has a new state: %s", lastUpdate.getID(), lastUpdate.getName(), lastUpdate.getState());
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				//logger.printf(Level.WARN, "%s", e);
			}
			
			//DO OTHER ANALISYS STUFF...
		}
	}


	@Override
	public void update(Entity entity) {
		monitorQueue.add(entity);
	}
}
