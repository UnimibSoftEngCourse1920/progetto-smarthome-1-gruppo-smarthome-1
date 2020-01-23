package com.unimib.emac;

import com.unimib.common.Observer;
import com.unimib.monitor.Monitor;
import com.unimib.test.Entity;


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
	public void updateAdd(Entity e) {
		evaluate(e);
	}

	@Override
	public void updateRemove(Entity e) {

	}
	
	public void evaluate(Entity e) {
		
	}

}
