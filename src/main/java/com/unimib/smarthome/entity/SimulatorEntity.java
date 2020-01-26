package com.unimib.smarthome.entity;

import com.unimib.smarthome.entity.enums.EntityType;

/**
 * Classe che rappresenta in astratto un'entita del simulatore (quindi compatibile col protocollo MQTT)
 */

public abstract class SimulatorEntity extends Entity{

	private String topic;
	private String state;
	
	public SimulatorEntity(EntityType type, int id, String name, String topic) {
		super(type, id, name);
		setTopic(topic);
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		//Controllo topic valido
		this.topic = topic;
	}

	public String getState() {
		return state;
	}

}
