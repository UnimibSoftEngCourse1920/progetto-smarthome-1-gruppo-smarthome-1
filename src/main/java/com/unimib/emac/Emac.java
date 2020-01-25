package com.unimib.emac;

import com.unimib.common.Observer;
import com.unimib.smarthome.entity.EntityManager;



public class Emac implements Observer {

	static Emac instance;
	public void setMappedEntityManager(EntityManager entityManager) {
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
