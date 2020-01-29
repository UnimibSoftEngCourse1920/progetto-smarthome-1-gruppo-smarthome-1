package com.unimib.smarthome.util;

import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.request.EntityCondition;
import com.unimib.smarthome.request.Request;

public class RequestValidator {

	private static EntityManager em = EntityManager.getInstance();

	private RequestValidator() {
	}

	/**
	 * 
	 * @param request richiesta da controllare
	 * @return true se tutte le condizioni della richiesta sono verificale, false
	 *         altrimenti
	 */

	public static boolean controlRequestConditions(Request request) {
		// Prendo la lista di tutte le condizioni della richiesta
		EntityCondition[] conditions = request.getConditions();
		// Se e' nullo, e' un comando utente, quindi va eseguito, senza condizioni.
		if (conditions != null)
			for (EntityCondition entityCondition : conditions) {
				if (!checkValidStatus(entityCondition))
					return false;
			}

		// Tutte le condizioni della richiesta sono rispettate
		return true;
	}

	private static boolean checkValidStatus(EntityCondition condition) {
		String entityRealStatus = em.getEntityState(condition.getEntityID());
		if(entityRealStatus != null) {
			char rel = condition.getRel();
			switch (rel) {
				case '=':
					return entityRealStatus.equals(condition.getState());
				case '>':
					return Double.valueOf(entityRealStatus) > Double.valueOf(condition.getState());
				case '<':
					return Double.valueOf(entityRealStatus) < Double.valueOf(condition.getState());
				default:
					return false;
			}
		}
		return true; //null è sempre valido come stato (?) :O
	}

}
