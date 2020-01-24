package com.unimib.smarthome.entity.exceptions;

import com.unimib.smarthome.entity.Entity;

public class DuplicatedEntityException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public DuplicatedEntityException(Entity e1, Entity e2) {
		super("Entity " + e1.getName() + " has the same id of " + e2.getName() + " [error on id: " + e1.getId() + "]");
	}

}
