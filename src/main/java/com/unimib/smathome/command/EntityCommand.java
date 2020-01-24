package com.unimib.smathome.command;

public class EntityCommand {
	private int EntityID;
	private String state;
	
	public int getEntityID() {
		return EntityID;
	}
	public void setEntityID(int entityID) {
		EntityID = entityID;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public void esegui() {}
}
