package com.unimib.smarthome;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.unimib.smarthome.broker.BrokerService;
import com.unimib.smarthome.console.CLIService;
import com.unimib.smarthome.emac.EMACService;
import com.unimib.smarthome.monitor.MonitorService;
import com.unimib.smarthome.sec.SECService;
import com.unimib.smarthome.systeminit.SystemInit;

public class SmartHome {
	
	private static Logger logger = LogManager.getLogger();
	private static BrokerService brokerService;
	private static CLIService cliService;
	private static MonitorService monitorService;
	private static SECService secService;
	private static EMACService emacService;
	
	public static void main(String[] args) {		
		logger.info("Starting SmartHome");
	
		SystemInit.initConfig();
		
		brokerService = new BrokerService();
		cliService = new CLIService();
		monitorService = new MonitorService();
		secService = new SECService();
		emacService = new EMACService();
		
		brokerService.start();
		cliService.start();
		monitorService.start();
		secService.start();
		emacService.start();
	}
	
	public static void shutdown() {
		brokerService.interrupt();
		cliService.interrupt();
		monitorService.interrupt();
		secService.interrupt();
		emacService.interrupt();
	}
}
