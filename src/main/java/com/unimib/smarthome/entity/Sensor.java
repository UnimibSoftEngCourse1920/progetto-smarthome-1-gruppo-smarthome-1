package com.unimib.smarthome.entity;

import com.unimib.smarthome.entity.enums.EntityType;

public class Sensor extends ReadOnlyEntity {

	public Sensor(EntityType type, int id, String name, String topic) {
		super(type, id, name, topic);
	}

	@Override
	protected void onIncomingMessage(String newState) {
		//E' un sensore, non fa nulla :)
	}

}
