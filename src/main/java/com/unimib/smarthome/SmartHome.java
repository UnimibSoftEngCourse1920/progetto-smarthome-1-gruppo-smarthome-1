package com.unimib.smarthome;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.unimib.smarthome.broker.BrokerService;
import com.unimib.smarthome.console.CLIService;
import com.unimib.smarthome.monitor.MonitorService;
import com.unimib.smarthome.sec.SECService;

public class SmartHome {
	
	static Logger logger = LogManager.getLogger();
	
	
	public static void main(String[] args) {		
		logger.printf(Level.INFO, "Starting SmartHome");
		
		BrokerService brokerService = new BrokerService();
		CLIService cliService = new CLIService();
		MonitorService monitorService = new MonitorService();
		SECService secService = new SECService();
		
		brokerService.start();
		cliService.start();
		monitorService.start();
		secService.start();
		
		
	}
}
