package com.unimib.smarthome.entities;

import com.unimib.smarthome.entity.CommandableEntity;
import com.unimib.smarthome.entity.enums.EntityType;
import com.unimib.smarthome.entity.exceptions.EntityIncomingMessageException;

public class Luce extends CommandableEntity{

		String state;
		
		public Luce(int id, String name, String topic, String initialState) {
			super(EntityType.BINARY, id, name, topic, "0");
		}

		@Override
		protected void onIncomingMessage(String newState) throws EntityIncomingMessageException {
			System.out.println("Setto nuovo stato su " + newState);
			this.setState(newState);
//			if(newState.equals("0") || newState.equals("1")) {
//				this.setState(newState);
//			}else
//				throw new EntityIncomingMessageException(this, "Invalid state: " + newState);
		}
		
		@Override
		public void setState(String newState) {
			this.state = newState;
		}
		
		@Override
		public String getState() {
			return state;
		}
		
	}