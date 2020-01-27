package com.unimib.smarthome.util;

import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.request.EntityCondition;
import com.unimib.smarthome.request.Request;

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
		EntityCondition[] conditions = request.getConditions();
		//Se è nullo, è un comando utente, quindi va eseguito, senza condizioni.
		if(conditions != null)
			for(EntityCondition entityCondition : conditions){
			//Itero tutte le condizioni
	
				String entityRealStatus = em.getEntityState(entityCondition.getEntityID());
				switch(entityCondition.getRel()) {
					case '=':
						if(!entityRealStatus.equals(entityCondition.getState()))
							return false;
					break;
					case '>':
						//Controllo in double così copro sia int che double
						if(Double.valueOf(entityRealStatus) <= Double.valueOf(entityCondition.getState()))
							return false;
					break;
					case '<':
						if(Double.valueOf(entityRealStatus) >= Double.valueOf(entityCondition.getState()))
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
