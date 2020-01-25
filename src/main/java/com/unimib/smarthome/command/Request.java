package com.unimib.smarthome.command;

public class Request {
	private EntityCondition[] condition;
	private EntityStatus[] then;
	private boolean retain;
	private int retainLevel;
	
	
	public Request(EntityCondition[] condition, EntityStatus[] then,boolean retain,int retainLevel )
	{
		this.setCondition(condition);
		this.setThen(then);
		this.setRetain(retain);
		this.setRetainLevel(retainLevel);
	}

	public void addAction(EntityStatus action) {
		
	}
	
	public void executeRequest() {}

	public EntityCondition[] getCondition() {
		return condition;
	}

	public void setCondition(EntityCondition[] condition) {
		this.condition = condition;
	}

	public EntityStatus[] getThen() {
		return then;
	}

	public void setThen(EntityStatus[] then) {
		this.then = then;
	}

	public boolean getRetain() {
		return retain;
	}

	public void setRetain(boolean retain) {
		this.retain = retain;
	}

	public int getRetainLevel() {
		return retainLevel;
	}

	public void setRetainLevel(int retainLevel) {
		this.retainLevel = retainLevel;
	};
	
	
	
	
	
	
	
	
}
