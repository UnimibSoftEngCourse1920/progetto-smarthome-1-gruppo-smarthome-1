package com.unimib.smarthome.console;
import java.util.Map;

import com.unimib.smarthome.entity.*;
public class CLIEvaluation {

	
	public void evaluation(String eval) {
		//divido la richiesta in base agli spazi.
		String[] e = eval.split(" ");
		CLIService s = new CLIService();
		CLIRequest r = new CLIRequest();
		/*
		 * se è list, richiamo la visualizzazione che è effettuata da CLIService.
		 * se è set, richiamo createRequest, bisogna vedere se retain e priority sono vuoti.
		 * se è get richiamo la visualizzazione dello stato di quell'entità.
		 */
		try {
			switch(e[0]) {
			case "list":
				Map<Integer, Entity> lista = EntityManager.getInstance().getEntityMap();
				s.entityVisualization(lista);
				break;
				
			case "set": 
				if(e.length == 5)
					r.createRequest(Integer.parseInt(e[1]), e[2], 
							Boolean.parseBoolean(e[3]), Integer.parseInt(e[4]));
				else
					r.createRequest(Integer.parseInt(e[1]), e[2]);
				break;
			case "get": 
				String state = EntityManager.getInstance().getEntityState(Integer.parseInt(e[1]));
				s.stateVisualization(Integer.parseInt(e[1]), state);
				break;
			default:
				//Nel caso in cui il comando inserito non è presente tra questi tre. 
				s.ErrorInput(eval);
			}
			
		} //Nel caso in cui abbia inserito un id non valido
		catch(Exception error) { s.Error(Integer.parseInt(e[1]));}
	}
}
	

