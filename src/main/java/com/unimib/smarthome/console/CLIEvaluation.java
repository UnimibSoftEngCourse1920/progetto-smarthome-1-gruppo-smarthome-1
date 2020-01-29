package com.unimib.smarthome.console;

import java.util.Map;

import com.unimib.smarthome.SmartHome;
import com.unimib.smarthome.entity.*;
import com.unimib.smarthome.request.Request;
import com.unimib.smarthome.sec.*;
public class CLIEvaluation {

	
	public void evaluation(String eval) {
		//divido la richiesta in base agli spazi.
		String[] e = eval.split(" ");
		CLIService s = new CLIService();
		CLIRequest r = new CLIRequest();
		SEC sec = SEC.getInstance();
		ConflictPool cf = new ConflictPool(sec);
		Request[] request;
		
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
					s.print(entity.toString())
				);
				break; 
				
			case "set": 
				if(e.length == 5) {
					s.print("Executing request.");
					r.createRequest(Integer.parseInt(e[1]), e[2], 
							Boolean.parseBoolean(e[3]), Integer.parseInt(e[4]));
				}
				else
					if(e.length == 3) {
						s.print("Executing request.");
						r.createRequest(Integer.parseInt(e[1]), e[2]);
					}
					else
						s.print("Set command must be formed as follows: set <entity> <value>.");
				break;
			case "get": 
					if(e[1] != null) {
						Entity entity = EntityManager.getInstance().getEntity(Integer.parseInt(e[1]));
						s.print(entity.toString());
					}
				break;
				
			case "shutdown":
				s.print("System is shutting down.");
				SmartHome.shutdown();
				break;
				
			case "listCF":
				request = cf.getConflictPool();
				if(request.length == 0)
					s.print("ConflictPool is empty.");
				else 
					for(Request req: request )
						s.print(req.toString());
					break;
					
			case "clearCF" : 
				cf.clearPool();
				s.print("ConflictPool is empty now. ");
				break;
				
			default:
				//Nel caso in cui il comando inserito non e' presente tra questi tre. 
				s.print("Input is invalid.");
			}
		} //Nel caso in cui abbia inserito un id non valido
		catch(Exception error) { s.print("The ID entered is not a valid ID.");
			
		} 
	}
}
	

