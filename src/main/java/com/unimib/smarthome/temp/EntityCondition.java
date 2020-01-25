package com.unimib.smarthome.temp;
public class EntityCondition extends EntityStatus {
	private char rel;

	public EntityCondition(int entityID, String state, char relazione) {
		super(entityID, state);
		this.setRel(relazione);
	}

	public char getRel() {
		return rel;
	}

	public void setRel(char rel) {
		this.rel = rel;
	}

}