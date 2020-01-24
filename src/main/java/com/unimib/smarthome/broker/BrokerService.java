package com.unimib.smarthome.broker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BrokerService extends Thread{
	
	BrokerManager brokerManager = BrokerManager.getInstance();
	Logger logger = LogManager.getLogger();
	
	@Override
	public void run() {
		logger.info("Starting Broker service");
		brokerManager.startBrokerServer();
		
		while(!Thread.interrupted()) {
			brokerManager.sendNewMessageToClients();
		}
		
	}
	
	
	
}
