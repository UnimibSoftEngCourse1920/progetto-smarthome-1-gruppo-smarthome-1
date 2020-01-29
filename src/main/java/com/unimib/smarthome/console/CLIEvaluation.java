package com.unimib.smarthome.console;

import java.util.Arrays;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.unimib.smarthome.SmartHome;
import com.unimib.smarthome.entity.*;
import com.unimib.smarthome.request.Request;
import com.unimib.smarthome.sec.*;
public class CLIEvaluation {
	private static Logger logger = LogManager.getLogger();
	final static Level CLI = Level.getLevel("CLI");
	private static Request pendingRequest = null;
	SEC sec = SEC.getInstance();
	ConflictPool cf = new ConflictPool(sec);
	Request[] request;
	//divido la richiesta in base agli spazi.
	
	CLIRequest r = new CLIRequest();
			
	public void evaluation(String eval) {
		String[] e = eval.split(" ");
		
		/*
		 * se e' list, richiamo la visualizzazione che e' effettuata da CLIService.
		 * se e' set, richiamo createRequest, bisogna vedere se retain e priority sono vuoti.
		 * se e' get richiamo la visualizzazione dello stato di quell'entita'.
		 */
		
		try {
			switch(e[0]) {  
			case "list":
				Map<Integer, Entity> list = EntityManager.getInstance().getEntityMap();
				list.forEach((key, entity) -> 
					print(entity.toString())
				);
				break; 
				
			case "set": 
				executeSet(e);
				
				break;
			case "get": 
					if(e[1] != null) {
						Entity entity = EntityManager.getInstance().getEntity(Integer.parseInt(e[1]));
						print(entity.toString());
					}
				break;
				
			case "shutdown":
				print("System is shutting down.");
				SmartHome.shutdown();
				break;
				
			case "listCF":
				printCF();
					break;
					
			case "clearCF" : 
				cf.clearPool();
				print("ConflictPool is empty now. ");
				break;
				
			case "accept":
				accept();
				break;
				
			case "refuse":
				refuse();
				break;
				
			default:
				//Nel caso in cui il comando inserito non e' presente tra questi tre. 
				print("Input is invalid.");
			}
		} //Nel caso in cui abbia inserito un id non valido
		catch(Exception error) { 
			print("The ID entered is not a valid ID.");
		} 
	}
	
	private void print(String s) {
		logger.printf(CLI, "%s", s);
	}
	
	public static void askPermission(Request r) {
		pendingRequest = r;
		logger.log(CLI, "*************************************************************************************************************");
		logger.printf(CLI, "Do you want to proceed with the request: %s ?", Arrays.toString(r.getConsequences()));
		logger.log(CLI, "*************************************************************************************************************");
			
	}
	
	private static void refuse() {
		logger.log(CLI, "Request rejected.");
		pendingRequest = null;
	}
	
	private static void accept() {
		if(pendingRequest != null) {
			Request newRequest = new Request(pendingRequest.getConditions(), pendingRequest.getConsequences(), pendingRequest.getRetain(), pendingRequest.getPriority());
			SEC.getInstance().addRequestToSECQueue(newRequest); 
			pendingRequest = null;
		}
	}
	private void printCF() {
		request = cf.getConflictPool();
		if(request.length == 0)
			print("ConflictPool is empty.");
		else 
			for(Request req: request )
				print(req.toString());
	}
	
	private void executeSet(String[] eval) {
		if(eval.length == 5) {
			print("Executing request.");
			r.createRequest(Integer.parseInt(eval[1]), eval[2], 
					Boolean.parseBoolean(eval[3]), Integer.parseInt(eval[4]));
		}
		else
			if(eval.length == 3) {
				print("Executing request.");
				r.createRequest(Integer.parseInt(eval[1]), eval[2]);
			}
			else
				print("Set command must be formed as follows: set <entity> <value>.");
	}
	
	public static boolean getPendingRequest() {
		boolean b = false;
		if (pendingRequest != null)
			b=true;
		return b;
	}
}
