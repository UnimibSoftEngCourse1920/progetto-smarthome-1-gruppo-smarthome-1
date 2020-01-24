package com.unimib.smarthome.broker;

public class BrokerService extends Thread{
	
	BrokerManager brokerManager = BrokerManager.getInstance();
	
	@Override
	public void run() {
		super.run();
		brokerManager.startBrokerServer();
	}
	
	
	
}
