package com.unimib.smarthome.entity;

import com.unimib.smarthome.entity.enums.EntityType;

/**
 * Classe che rappresenta in astratto un'entita del simulatore (quindi compatibile col protocollo MQTT)
 */

public abstract class SimulatorEntity extends Entity{

	private String topic;

	public SimulatorEntity(EntityType type, int id, String name, String topic) {
		super(type, id, name);
		setTopic(topic);
	}
	
	public SimulatorEntity(EntityType type, int id, String name, String topic, String initialState) {
		super(type, id, name);
		setTopic(topic);
		setState(initialState);
	}

	public String getTopic() {
		return topic;
	}

	protected void setTopic(String topic) {
		this.topic = topic;
	}
	
	@Override
	public String toString() {
		return "Entity " + getID() + "[name: " + getName() + ", type:" + getType() + ", state: " + getState() + ", topic:" + getTopic() + "]";
	}
}
