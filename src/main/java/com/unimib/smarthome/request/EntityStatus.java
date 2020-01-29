package com.unimib.smarthome.request;

public class EntityStatus {
	private int entityID;
	private String state;
	
	public EntityStatus(int entityID, String state) {
		this.entityID = entityID;
		this.state = state;
	}
	
	public int getEntityID() {
		return entityID;
	}

	public String getState() {
		return state;
	}

	@Override
	public String toString() {
		return "EntityStatus [entityID: " + this.getEntityID() + ", state: " + this.getState() + "]";
	}
}
