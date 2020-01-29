package com.unimib.smarthome.util;

import com.unimib.smarthome.entity.Entity;
import com.unimib.smarthome.entity.EntityManager;
import com.unimib.smarthome.entity.enums.EntityType;
import com.unimib.smarthome.entity.exceptions.EntityIncomingMessageException;
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
		
		Entity conditionEntity = em.getEntity(condition.getEntityID());
		ComparatorEntity comparatorEntity = new ComparatorEntity(condition);
		
		if(conditionEntity != null) {
			char rel = condition.getRel();
			switch (rel) {
				case '=':
					return conditionEntity.compareTo(comparatorEntity) == 0;
					
				case '>':
					return  conditionEntity.compareTo(comparatorEntity) > 0;
					
				case '<':
					return conditionEntity.compareTo(comparatorEntity) < 0;
				default:
					return false;
			}
		}
		return true; 
	}
	
	private static class ComparatorEntity extends Entity {
		public ComparatorEntity(EntityCondition ec) {
			super(EntityType.BINARY, ec.getEntityID(), "comparatorEntity", ec.getState());
		}

		@Override
		public int compareTo(Entity o) {
			return 0;
		}

		@Override
		protected <T> Entity onIncomingMessage(String newState, Class<T> source) throws EntityIncomingMessageException {
			return null;
		}

		
	}

}
