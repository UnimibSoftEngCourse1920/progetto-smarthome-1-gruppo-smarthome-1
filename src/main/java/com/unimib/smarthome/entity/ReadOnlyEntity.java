package com.unimib.smarthome.entity;

import com.unimib.smarthome.entity.enums.EntityType;

/**
 * Classe che rappresenta in astratto un'entita del di sola lettura
 */

public abstract class ReadOnlyEntity extends SimulatorEntity{
	
	public ReadOnlyEntity(EntityType type, int id, String name, String topic) {
		super(type, id, name, topic);
	}
	
	@Override
	public String toString() {
		return "ReadOnlyEntity [id = " + this.getId() + " type=" + this.type + ", name= " + this.getName() + ", topic= " + this.getTopic() + ",state=" + this.getState() + "]";
	}
	
}
