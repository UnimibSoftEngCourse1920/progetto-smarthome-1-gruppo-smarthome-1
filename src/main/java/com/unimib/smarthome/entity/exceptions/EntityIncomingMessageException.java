package com.unimib.smarthome.entity.exceptions;

import com.unimib.smarthome.entity.Entity;

public class EntityIncomingMessageException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public EntityIncomingMessageException(Entity e, String error) {
		super("Error on entity " + e.getID() + " (" + e.getName() + ") state: " + e.getState() + " reason: " + error);
	}
	
}
