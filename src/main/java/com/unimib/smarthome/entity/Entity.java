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
		this.id = id;
		setType(type);
		setName(name);
		setState("");
	}
	
	//Metodo per permettere all'entita di comunicare con l'esterno
	protected abstract <T> void onIncomingMessage(String newState, Class<T> source) throws EntityIncomingMessageException;

	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getState() {
		return state;

	}
	
	public String getType() {
		return this.type.getName();
	}

	protected void setName(String name) {
		this.name = name;
	}
	
	protected void setType(EntityType type) {
		this.type = type;
	}
	
	protected void setState(String state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return "Entity " + getID() + "[name: " + getName() + ", type:" + getType() + ", state: " + getState() + "]";
	}
}