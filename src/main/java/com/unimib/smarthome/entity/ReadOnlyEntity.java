package com.unimib.smarthome.entity;

import com.unimib.smarthome.entity.enums.EntityType;

/**
 * Classe che rappresenta in astratto un'entita del di sola lettura
 */

public abstract class ReadOnlyEntity extends SimulatorEntity{
	
	private String state;
	
	public ReadOnlyEntity(EntityType type, int id, String name, String topic) {
		super(type, id, name, topic, "");
	}
	
	public ReadOnlyEntity(EntityType type, int id, String name, String topic, String initialState) {
		super(type, id, name, topic);
		state = initialState;
	}
	
	@Override
	public String toString() {
		return "ReadOnlyEntity [id: " + this.getID() + ", name: \"" + this.getName() + "\", state: " + this.getState() + ", topic: " + this.getTopic() + ", type: " + this.getType() + "]";
	}
	
	
	@Override
	public String getState() {
		return state;
	}
}

