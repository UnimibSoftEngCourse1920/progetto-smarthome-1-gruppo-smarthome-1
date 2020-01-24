package com.unimib.smarthome.entity;

import com.unimib.smarthome.entity.enums.EntityType;

/**
 * Classe che rappresenta in astratto un'entita del simulatore comandabile
 */


public abstract class CommandableEntity extends SimulatorEntity{

	private String state;
	
	public CommandableEntity(EntityType type, int id, String name, String topic) {
		super(type, id, name, topic);
		this.state = "0";
	}
	
	public CommandableEntity(EntityType type, int id, String name, String topic, String initialState) {
		this(type, id, name, topic);
		this.state = initialState;
	}

	public void setState(String newState) {
		this.state = newState;
	}

	@Override
	public String toString() {
		return "CommandableEntity [id = " + this.getId() + " type=" + this.type + ", name= " + this.getName() + ", topic= " + this.getTopic() + ",state=" + state + "]";
	}

	
}
