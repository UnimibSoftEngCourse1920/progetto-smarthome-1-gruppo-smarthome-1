package com.unimib.smarthome.emac;

import com.unimib.smarthome.entity.EntityManager;

public class EMACService extends Thread{

	Emac emac = Emac.getInstance();
	
	@Override
	public void run() {
		EntityManager.getInstance().attach(emac);
		while(!Thread.interrupted()) {
			emac.controlNewStatus();
		}
	}
	
}
