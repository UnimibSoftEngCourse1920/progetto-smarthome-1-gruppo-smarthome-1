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
		 * se e' list, richiamo la visualizzazione che e' effettuata da CLIService.
		 * se e' set, richiamo createRequest, bisogna vedere se retain e priority sono vuoti.
		 * se e' get richiamo la visualizzazione dello stato di quell'entita'.
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
					if(e.length == 3)
						r.createRequest(Integer.parseInt(e[1]), e[2]);
					else
						s.errorSet();
					
				break;
			case "get": 
				if(e[1] != null) {
					Entity entity = EntityManager.getInstance().getEntity(Integer.parseInt(e[1]));
					s.stateVisualization(Integer.parseInt(e[1]), entity);
				}
				else
					s.errorGet();
					
				break;
			default:
				//Nel caso in cui il comando inserito non e' presente tra questi tre. 
				s.errorInput(eval);
			}
			
			
			//TODO comando per spegnere tutto, e avere lista richieste in CF e svuotarla
			
		} //Nel caso in cui abbia inserito un id non valido
		catch(Exception error) { s.error(Integer.parseInt(e[1]));}
	}
}
	

