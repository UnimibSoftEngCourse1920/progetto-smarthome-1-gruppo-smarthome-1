package com.unimib.smarthome.util;

import java.util.Iterator;
import java.util.LinkedList;
import com.unimib.smarthome.temp.*;
import com.unimib.smarthome.entity.EntityManager;

public class RequestValidator {
	

	private static EntityManager em = EntityManager.getInstance();
	
	private RequestValidator() {}
	
	/**
	 * 
	 * @param request richiesta da controllare
	 * @return true se tutte le condizioni della richiesta sono verificale, false altrimenti
	 */
	
	public static boolean controlRequestConditions(Request request) {
		//Prendo la lista di tutte le condizioni della richiesta
		LinkedList<EC> conditions = request.conditions;
		Iterator<EC> iterator = conditions.iterator();
		
		//Itero tutte le condizioni
		while(iterator.hasNext()) {
			
			//Per ogni condizione controllo se essa è verificata (le condizioni sono tutte in AND)
			EC entityCondition = iterator.next();
			String entityRealStatus = em.getEntityState(entityCondition.id);
			switch(entityCondition.relation) {
				case "=":
					if(!entityRealStatus.equals(entityCondition.state))
						return false;
				break;
				case ">":
					//Controllo in double così copro sia int che double
					if(Double.valueOf(entityRealStatus) <= Double.valueOf(entityCondition.state))
						return false;
				break;
				case "<":
					if(Double.valueOf(entityRealStatus) >= Double.valueOf(entityCondition.state))
						return false;
				break;
				default: 
					//Operatore relazione non supportato
					return false;
			}
			
		}
		
		//Tutte le condizioni della richiesta sono rispettate
		return true;
	}
	
	
}
