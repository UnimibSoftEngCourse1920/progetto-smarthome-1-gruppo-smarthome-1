package com.unimib.smarthome.entity;

import com.unimib.smarthome.entity.enums.EntityType;
import com.unimib.smarthome.entity.exceptions.EntityIncomingMessageException;

public class Device extends CommandableEntity {
	
	public Device(EntityType type, int id, String name, String topic) {
		super(type, id, name, topic, "0");
	}
	
	public Device(EntityType type, int id, String name, String topic, String initialState) {
		super(type, id, name, topic, initialState);
	}
	
	@Override 
	protected Device setState(String state) throws EntityIncomingMessageException{
		switch(getType()) {
			case BINARY:
				if(state == "0" || state == "1")
					return new Device(this.getType(), this.getID(), this.getName(), this.getTopic(), state);
				throw new EntityIncomingMessageException(this, "BINARY entity cannot have state " + state);
			case RANGE:
			case STATE:
			default:
				return new Device(this.getType(), this.getID(), this.getName(), this.getTopic(), state);
		}
	}
	
	@Override
	protected <T> Device onIncomingMessage(String newState, Class<T> source) throws EntityIncomingMessageException {
		return this.setState(newState);
	}
	
}
