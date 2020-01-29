package com.unimib.smarthome.entities;

import com.unimib.smarthome.entity.CommandableEntity;
import com.unimib.smarthome.entity.enums.EntityType;
import com.unimib.smarthome.entity.exceptions.EntityIncomingMessageException;

public class Luce extends CommandableEntity{

	public Luce(int id, String name, String topic, String state) {
		super(EntityType.STATE, id, name, topic, state);
	}
	
	public Luce(int id, String name, String topic) {
		super(EntityType.STATE, id, name, topic, "0");
	}

	@Override 
	protected Luce setState(String state) throws EntityIncomingMessageException{
		switch(getType()) {
		case BINARY:
			if(state == "0" || state == "1")
				return new Luce(this.getID(), this.getName(), this.getTopic(), state);
			throw new EntityIncomingMessageException(this, "BINARY entity cannot have state " + state);
		case RANGE:
		case STATE:
		default:
			return new Luce(this.getID(), this.getName(), this.getTopic(), state);
		}
	}
}