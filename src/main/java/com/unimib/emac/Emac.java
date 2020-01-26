package com.unimib.emac;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unimib.action.Request;
import com.unimib.common.Observer;

import com.unimib.smarthome.entity.EntityManager;



public class Emac implements Observer {

	static Emac instance;
	private Map<Integer, List<Request>> requests = new HashMap<>();
	
	


	//Singleton
	public static Emac getInstance() {
		if(instance == null) {				
			instance = new Emac();
			}
		return instance;
	}
	
	public void registerAutomation(Request r) {
		//r.conditions.getEntityID();
		
		
		
		
	}

	
	public void evaluate(EntityManager e) {

	}

}
