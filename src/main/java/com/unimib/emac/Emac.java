package com.unimib.emac;



import com.unimib.common.Observer;
import com.unimib.monitor.Monitor;
import com.unimib.smarthome.entity.EntityManager;



public class Emac implements Observer {

	static Emac instance;
	
	


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
