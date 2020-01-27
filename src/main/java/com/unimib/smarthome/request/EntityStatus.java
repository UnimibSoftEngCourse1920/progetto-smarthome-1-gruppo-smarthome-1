package com.unimib.smarthome.request;

public class EntityStatus {
	private int entityID;
	private String state;
	
	public EntityStatus(int entityID, String state) {
		this.setEntityID(entityID);
		this.setState(state);
	}
	
	public int getEntityID() {
		return entityID;
	}
	public void setEntityID(int entityID) {
		this.entityID = entityID;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public void esegui() {}
	
	@Override
	public String toString() {
		return "EntityStatus [entityID: " + this.getEntityID() + ", state: " + this.getState() + "]";
	}
}
