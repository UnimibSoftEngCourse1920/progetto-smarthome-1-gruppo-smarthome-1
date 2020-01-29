package com.unimib.smarthome.entity;

import com.unimib.smarthome.entity.enums.EntityType;
import com.unimib.smarthome.entity.exceptions.EntityIncomingMessageException;

/**
 * Classe che rappresenta in astratto un'entita
 */

public abstract class Entity {

	private int id;
	private EntityType type;
	private String name;
	private String state;
	
	public Entity(EntityType type, int id, String name) {
		this(type, id, name, "");
	}
	
	public Entity(EntityType type, int id, String name, String state) {
		this.id = id;
		this.type = type;
		this.id = id;
		this.state = state;
	}
	
	//Metodo per permettere all'entita di comunicare con l'esterno
	protected abstract <T> Entity onIncomingMessage(String newState, Class<T> source) throws EntityIncomingMessageException;

	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getState() {
		return state;

	}
	
	public EntityType getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return "Entity " + getID() + "[name: " + getName() + ", type:" + getType() + ", state: " + getState() + "]";
	}
}