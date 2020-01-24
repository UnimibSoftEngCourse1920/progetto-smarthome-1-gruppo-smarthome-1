package com.unimib.action;

import com.unimib.common.Command;

public class Action implements Command {
	private int entityState;
	private String entityID;
	

	public Action(int entityState, String entityID) {
		super();
		setEntityState(entityState);
		setEntityID(entityID);
	}


	public int getEntityState() {
		return entityState;
	}


	public void setEntityState(int entityState) {
		this.entityState = entityState;
	}


	public String getEntityID() {
		return entityID;
	}


	public void setEntityID(String entityID) {
		this.entityID = entityID;
	}


	@Override
	public void execute() {
		
		
	}
	

}
