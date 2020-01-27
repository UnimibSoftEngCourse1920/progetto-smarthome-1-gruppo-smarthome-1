package com.unimib.smarthome.console;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.entity.Entity;

public class CLIService extends Thread {
	
	private Logger logger = LogManager.getLogger();
	final static Level CLI = Level.getLevel("CLI");

	@Override
	public void run() {
		logger.info("Starting cli interface");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String input;
		while (!Thread.interrupted()) {
			try {
				
				logger.log(CLI, "Enter a command:");
				input = reader.readLine();
				CLIEvaluation eval = new CLIEvaluation();
				eval.evaluation(input);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//Visualizzo tutte le entità
	public void entityVisualization(Map<Integer, Entity> lista ) {
		
		for(Integer key : lista.keySet()) {

			logger.printf(CLI, "ID entita': %d", lista.get(key).getID());
			logger.printf(CLI, "Nome entita': %s", lista.get(key).getName());
			logger.printf(CLI, "Stato entita': %s", lista.get(key).getState());
			logger.printf(CLI, "Tipo entita': %s", lista.get(key).getType());

	  }
	}
	// visualizzo lo stato dell'entità che l'utente richiede.
	public void stateVisualization(int id, String state) {
		logger.printf(CLI, "L'entita' con ID: %d ha come stato %s", id,state);
	}
	
	public void errorInput(String input) {
		logger.printf(CLI, "L'input %s non è corretto.", input);
	}
	
	public void error(int input) {
		logger.printf(CLI, "l'id %d inserito non è corretto.", input);
	}
	
	public void errorSet() {
		logger.printf(CLI, "Il comando set, deve essere del tipo: set <entity> <value>." );
	}
	
	public void errorGet() {
		logger.printf(CLI, "Il comando get, deve essere del tipo: get <entity>." );
	}
}
