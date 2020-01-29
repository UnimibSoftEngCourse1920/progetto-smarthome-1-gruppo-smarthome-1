package com.unimib.smarthome.console;
import java.util.Scanner;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class CLIService extends Thread {

	private Logger logger = LogManager.getLogger();
	final static Level CLI = Level.getLevel("CLI");
	CLIEvaluation eval = new CLIEvaluation();
	
	@Override
	public void run() {
		logger.info("Starting client interface");

		Scanner sc = new Scanner(System.in);
		
		String input;
		while (!Thread.interrupted()) {
			logger.log(CLI, "Enter a command:");
			input = sc.nextLine();
			if(!input.equals(""))
				eval.evaluation(input); 
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				sc.close();
				this.interrupt();
			}
		}
	}
}
