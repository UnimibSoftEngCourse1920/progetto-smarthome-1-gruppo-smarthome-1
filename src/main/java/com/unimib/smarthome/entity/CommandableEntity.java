package com.unimib.smarthome.entity;

import com.unimib.smarthome.entity.enums.EntityType;
import com.unimib.smarthome.entity.exceptions.EntityIncomingMessageException;

/**
 * Classe che rappresenta in astratto un'entita del simulatore comandabile
 */


public abstract class CommandableEntity extends SimulatorEntity{

	private String state;
	
	public CommandableEntity(EntityType type, int id, String name, String topic) {
		super(type, id, name, topic, "0");
	}
	
	public CommandableEntity(EntityType type, int id, String name, String topic, String initialState) {
		super(type, id, name, topic);
		this.state = initialState;
	}
	
	@Override
	protected <T> Entity onIncomingMessage(String newState, Class<T> source) throws EntityIncomingMessageException {
		return this.setState(newState);
	}
	
	@Override
	public String getState() {
		return state;
	}
	
	private Entity setState(String newState) throws EntityIncomingMessageException{
		try {
			return getClass().getDeclaredConstructor(EntityType.class, Integer.class, String.class, String.class, String.class).newInstance(getType(), this.getID(), this.getName(), this.getTopic(), newState);
		} catch (Exception e) {
			return this;
		}
	}
	
	@Override
	public String toString() {
		return "CommandableEntity [id: " + this.getID() + ", name: \"" + this.getName() + "\", state: " + this.getState() + ", topic: " + this.getTopic() + ", type: " + this.getType() + "]";
	}
	
}
