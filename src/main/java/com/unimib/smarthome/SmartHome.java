package com.unimib.smarthome;
import com.unimib.smarthome.broker.BrokerService;
import com.unimib.smarthome.console.CLIService;

public class SmartHome {
	
	public static void main(String[] args) {
		
		
		BrokerService brokerService = new BrokerService();
		CLIService cliService = new CLIService();
		
		brokerService.start();
		cliService.start();
	}
}
