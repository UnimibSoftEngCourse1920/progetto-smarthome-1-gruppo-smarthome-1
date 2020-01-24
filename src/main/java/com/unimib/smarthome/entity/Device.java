package com.unimib.smarthome.entity;

import com.unimib.smarthome.entity.enums.EntityType;

public class Device extends CommandableEntity {

	
	public Device(EntityType type, int id, String name, String topic) {
		super(type, id, name, topic);
		
	}

	@Override
	void onIncomingMessage(String newState) {
		this.setState(Integer.parseInt(newState));
	}

	

}
