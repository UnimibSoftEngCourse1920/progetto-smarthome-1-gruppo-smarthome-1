package com.unimib.emac;

import com.unimib.common.Observer;
import com.unimib.monitor.Monitor;
import com.unimib.smarthome.entity.EntityManager;



public class Emac implements Observer {

	static Emac instance;
	private Monitor monitor;
	
	//Singleton
		public static Emac getInstance() {
			if(instance == null) {
				instance = new Emac();
			}
			return instance;
		}
	
	
	@Override
	public void updateAdd(EntityManager e) {
		evaluate(e);
	}

	@Override
	public void updateRemove(EntityManager e) {

	}
	
	public void evaluate(EntityManager e) {
		
	}

}
