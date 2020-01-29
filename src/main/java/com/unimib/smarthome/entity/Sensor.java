package com.unimib.smarthome.entity;

import com.unimib.smarthome.broker.BrokerManager;
import com.unimib.smarthome.entity.enums.EntityType;

public class Sensor extends ReadOnlyEntity {

	String state;
	
	public Sensor(EntityType type, int id, String name, String topic) {
		super(type, id, name, topic, "");
	}
	
	public Sensor(EntityType type, int id, String name, String topic, String newState) {
		super(type, id, name, topic);
		this.state = newState;
	}
	
	@Override
	protected <T> Sensor onIncomingMessage(String newState, Class<T> source) {
		if(source.equals(BrokerManager.class)) {
			return new Sensor(this.getType(), this.getID(), this.getName(), this.getTopic(), newState);
		}
		return this;
	}
	
	@Override
	public String getState() {
		return state;
	}

}
