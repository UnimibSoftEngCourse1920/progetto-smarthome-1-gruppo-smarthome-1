package com.unimib.smathome.command;

public class Request {
	private EntityCondition[] condition;
	private EntityCommand[] then;
	private boolean retain;
	private int retainLevel;
	
	
	public Request(EntityCondition[] condition, EntityCommand[] then,boolean retain,int retainLevel )
	{
		this.setCondition(condition);
		this.setThen(then);
		this.setRetain(retain);
		this.setRetainLevel(retainLevel);
	}
	
	
	public void addAction(EntityCommand action) {
		
	}
	
	public void executeRequest() {}

	public EntityCondition[] getCondition() {
		return condition;
	}

	public void setCondition(EntityCondition[] condition) {
		this.condition = condition;
	}

	public EntityCommand[] getThen() {
		return then;
	}

	public void setThen(EntityCommand[] then) {
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
