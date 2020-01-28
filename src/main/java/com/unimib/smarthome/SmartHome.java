package com.unimib.smarthome;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.unimib.smarthome.broker.BrokerService;
import com.unimib.smarthome.console.CLIService;
import com.unimib.smarthome.entity.Device;
import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.entity.enums.EntityType;
import com.unimib.smarthome.entity.exceptions.DuplicatedEntityException;
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
		
		Device ce = new Device(EntityType.STATE, 15, "Allarme", "alarms/alarm1");
		try {
			EntityManager.getInstance().registerEntity(ce);
		} catch (DuplicatedEntityException e) {
			e.printStackTrace();
		}
	
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
