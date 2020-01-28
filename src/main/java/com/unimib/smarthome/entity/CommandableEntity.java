package com.unimib.smarthome.entity;

import com.unimib.smarthome.entity.enums.EntityType;

/**
 * Classe che rappresenta in astratto un'entita del simulatore comandabile
 */


public abstract class CommandableEntity extends SimulatorEntity{

	private String state;
	
	public CommandableEntity(EntityType type, int id, String name, String topic) {
		super(type, id, name, topic);
		setState("0");
	}
	
	protected void setState(String newState) {
		this.state = newState;
	}
	
	public String getState() {
		return state;
	}

	@Override
	public String toString() {
		return "CommandableEntity [id: " + this.getID() + ", name: \"" + this.getName() + "\", state: " + this.getState() + ", topic: " + this.getTopic() + ", type: " + this.getType() + "]";
	}

	@Override
	protected <T> void onIncomingMessage(String newState, Class<T> source) {
		this.setState(newState);
	}

	
}
