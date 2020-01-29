package com.unimib.smarthome.entity;

import com.unimib.smarthome.entity.enums.EntityType;

/**
 * Classe che rappresenta in astratto un'entita del di sola lettura
 */

public abstract class ReadOnlyEntity extends SimulatorEntity{

	public ReadOnlyEntity(EntityType type, int id, String name, String topic) {
		super(type, id, name, topic, "");
	}
	
	public ReadOnlyEntity(EntityType type, int id, String name, String topic, String initialState) {
		super(type, id, name, topic, initialState);
	}
	
	@Override
	public String toString() {
		return "ReadOnlyEntity [id: " + this.getID() + ", name: \"" + this.getName() + "\", state: " + this.getState() + ", topic: " + this.getTopic() + ", type: " + this.getType() + "]";
	}
	
}

