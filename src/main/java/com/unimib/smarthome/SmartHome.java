package com.unimib.smarthome;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.unimib.smarthome.broker.BrokerService;
import com.unimib.smarthome.console.CLIService;
import com.unimib.smarthome.monitor.MonitorService;
import com.unimib.smarthome.sec.SECService;
import com.unimib.smarthome.systeminit.SystemInit;

public class SmartHome {
	
	private static Logger logger = LogManager.getLogger();
	private static BrokerService brokerService;
	private static CLIService cliService;
	private static MonitorService monitorService;
	private static SECService secService;
	
	public static void main(String[] args) {		
		logger.info("Starting SmartHome");
		
		SystemInit.initilizer();

		brokerService = new BrokerService();
		cliService = new CLIService();
		monitorService = new MonitorService();
		secService = new SECService();
		
		brokerService.start();
		cliService.start();
		monitorService.start();
		secService.start();
	}
	
	public static void shutdown() {
		brokerService.interrupt();
		cliService.interrupt();
		monitorService.interrupt();
		secService.interrupt();
		
	}
	
	
}
