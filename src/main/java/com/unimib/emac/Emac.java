package com.unimib.emac;

import java.util.HashMap;

import com.unimib.common.Observer;
import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.monitor.MonitorService;



public class Emac implements Observer {

	static Emac instance;
	private MonitorService monitor;
	private EntityManager entityManager;
	


	

	public void setMappedEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	//Singleton
	public static Emac getInstance() {
		if(instance == null) {				
			instance = new Emac();
			}
		return instance;
	}
	
	

	
	public void evaluate(EntityManager e) {
	
	}

}
