package com.unimib.smarthome.monitor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.common.Observer;
import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.request.EntityCondition;

public class MonitorService extends Thread implements Observer  {
	private Logger logger = LogManager.getLogger();
	private final Level MONITOR_LEVEL = Level.getLevel("MONITOR");
	private ConcurrentLinkedQueue<EntityCondition> monitorQueue = new ConcurrentLinkedQueue<>();
	EntityCondition lastUpdate = null;


	@Override
	public void run() {
		logger.info("Starting monitor service");
		while(!Thread.interrupted()) {
			
			if((lastUpdate = monitorQueue.poll()) != null ) {
					logger.printf(MONITOR_LEVEL, "Sensor id %i has a new state: %s", lastUpdate.getEntityID(), lastUpdate.getState());
			}
			
			//DO OTHER ANALISYS STUFF...
			
		}
	}


	@Override
	public void update(EntityCondition ec) {
		monitorQueue.add(ec);
	
	}
}
