package com.unimib.monitor;


import java.util.HashMap;



import com.unimib.common.Observer;
import com.unimib.common.Subject;
import com.unimib.emac.Emac;
import com.unimib.smarthome.entity.EntityManager;





public class Monitor implements Observer {
	static Monitor instance;
	private Emac emac;
	private HashMap<Integer, EntityManager> mappedEntityManager;
	
	
	
	
	public HashMap<Integer, EntityManager> getMappedEntity() {
		return mappedEntityManager;
	}
	

	public void setMappedEntity(HashMap<Integer, EntityManager> mappedEntity) {
		this.mappedEntityManager = mappedEntity;
	}


	private Monitor() {
		this.setMappedEntity(new HashMap<Integer, EntityManager>());
	}
	
	//Singleton
	public static Monitor getInstance() {
		if(instance == null) {
			instance = new Monitor();
		}
		return instance;
	}
	
	
	
	
	
}	


