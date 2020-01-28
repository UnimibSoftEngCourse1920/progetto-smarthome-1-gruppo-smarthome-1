package com.unimib.smarthome.entity;

import com.unimib.smarthome.broker.BrokerManager;
import com.unimib.smarthome.entity.enums.EntityType;

/**
 * Classe che rappresenta in astratto un'entita del di sola lettura
 */

public abstract class ReadOnlyEntity extends SimulatorEntity{
	
	private String state;
	
	public ReadOnlyEntity(EntityType type, int id, String name, String topic) {
		super(type, id, name, topic);
		state = "";
	}
	
	@Override
	public String toString() {
		return "ReadOnlyEntity [id: " + this.getID() + ", name: \"" + this.getName() + "\", state: " + this.getState() + ", topic: " + this.getTopic() + ", type: " + this.getType() + "]";
	}
	
	@Override
	protected <T> void onIncomingMessage(String newState, Class<T> source) {
		System.out.println(source);
		if(source.equals(BrokerManager.class)) {
			state = newState;
		}
	}
	
	@Override
	public String getState() {
		return state;
	}
}

