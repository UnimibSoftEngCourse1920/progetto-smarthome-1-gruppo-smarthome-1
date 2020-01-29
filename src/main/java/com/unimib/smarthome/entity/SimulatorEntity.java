package com.unimib.smarthome.entity;

import com.unimib.smarthome.entity.enums.EntityType;

/**
 * Classe che rappresenta in astratto un'entita del simulatore (quindi compatibile col protocollo MQTT)
 */

public abstract class SimulatorEntity extends Entity{

	private String topic;

	public SimulatorEntity(EntityType type, int id, String name, String topic, String initialState) {
		super(type, id, name, initialState);
		this.topic = topic;
	}
	
	public SimulatorEntity(EntityType type, int id, String name, String topic) {
		this(type, id, name, topic, "0");
	}
	
	public String getTopic() {
		return topic;
	}
	
	@Override
	public String toString() {
		return "Entity " + getID() + "[name: " + getName() + ", type:" + getType() + ", state: " + getState() + ", topic:" + getTopic() + "]";
	}
	
	@Override
	public int compareTo(Entity o) {
		return (int) (Double.parseDouble(this.getState()) - Double.parseDouble(o.getState())); 
	}
}

