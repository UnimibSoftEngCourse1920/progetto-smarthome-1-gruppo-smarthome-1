package com.unimib.smarthome.entity;

import com.unimib.smarthome.entity.enums.EntityType;
import com.unimib.smarthome.entity.exceptions.EntityIncomingMessageException;

/**
 * Classe che rappresenta in astratto un'entita
 */

public abstract class Entity {

	private int id;
	public EntityType type;
	private String name;
	private String state;
	
	public Entity(EntityType type, int id, String name) {
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
	//Metodo per permettere all'entita di comunicare con l'esterno
	abstract protected void onIncomingMessage(String newState) throws EntityIncomingMessageException; 
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getState() {
		return this.state;
	}
	
	public String getType() {
		return this.type.getName();
	}

}
