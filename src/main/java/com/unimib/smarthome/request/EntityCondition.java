package com.unimib.smarthome.request;

public class EntityCondition extends EntityStatus {
	private char rel;
	
	public EntityCondition(int entityID, String state, char relationship) {
		super(entityID, state);
		this.setRel(relationship);
	}

	public char getRel() {
		return rel;
	}

	public void setRel(char rel) {
		this.rel = rel;
	}
	
	@Override
	public String toString() {
		return "EntityCondition [entityID: " + getEntityID() + ", state: " + getState() + ", relation: " + this.getRel() + "]";
	}

}
