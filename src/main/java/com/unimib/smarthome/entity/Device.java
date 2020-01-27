package com.unimib.smarthome.entity;

import com.unimib.smarthome.entity.enums.EntityType;

public class Device extends CommandableEntity {

	
	public Device(EntityType type, int id, String name, String topic, String state) {
		super(type, id, name, topic, state);
		
	}

	@Override
	protected void onIncomingMessage(String newState) {
		this.setState(newState);
	}

	

}
