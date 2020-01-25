package com.unimib.smarthome.temp;
public class Request {
	private EntityCondition[] conditions;
	private EntityStatus[] consequences;
	private boolean retain;
	private int priority;
	
	
	public Request(EntityCondition[] conditions, EntityStatus[] consequences, boolean retain, int priority )
	{
		this.setConditions(conditions);
		this.setConsequences(consequences);
		this.setRetain(retain);
		this.setPriority(priority);
	}

	public void addAction(EntityStatus action) {
		
	}
	
	public void executeRequest() {}

	public EntityCondition[] getCondition() {
		return conditions;
	}

	public void setConditions(EntityCondition[] condition) {
		this.conditions = condition;
	}

	public EntityStatus[] getConsequences() {
		return consequences;
	}

	public void setConsequences(EntityStatus[] then) {
		this.consequences = then;
	}

	public boolean getRetain() {
		return retain;
	}

	public void setRetain(boolean retain) {
		this.retain = retain;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	};
	
	
	
	
	
	
	
	
}