package com.unimib.smarthome.console;
import java.util.Map;

import com.unimib.smarthome.entity.*;
public class CLIEvaluation {
	
	
	
	public void evaluation(String eval) {
		String[] e = eval.split(" ");
		CLIService s = new CLIService();
		CLIRequest r = new CLIRequest();
		
		switch(e[0]) {
		case "list":
			Map<Integer, Entity> lista = EntityManager.getInstance().getEntityMap();
			s.entityVisualization(lista);
			break;
		case "set": 
			if(e[3] != null && e[4] != null)
				r.createRequest(Integer.parseInt(e[1]), e[2], 
						Boolean.parseBoolean(e[3]), Integer.parseInt(e[4]));
			else
				r.createRequest(Integer.parseInt(e[1]), e[2]);
			break;
		case "get": 
			String state = EntityManager.getInstance().getEntityState(Integer.parseInt(e[1]));
			s.stateVisualization(Integer.parseInt(e[1]), state);
			break;
		}
	}
	
}
