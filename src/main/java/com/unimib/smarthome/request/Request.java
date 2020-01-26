package com.unimib.smarthome.request;
import com.unimib.smarthome.sec.*;
public class Request {
	private EntityCondition[] conditions;
	private EntityStatus[] consequences;
	private boolean retain;
	private int priority;
	
	
	
	public Request(EntityCondition[] condition, EntityStatus[] then,boolean retain,int priority ){
		this.setCondition(condition);
		this.setConsequences(then);
		this.setRetain(retain);
		this.setPriority(priority);
	}

	public void addEntityStatus(EntityStatus action) {
		//che deve fare?
	}
	
	public void executeRequest() {
		SEC.getInstance().evaluateRequest(this);
	}

	public EntityCondition[] getCondition() {
		return conditions;
	}

	public void setCondition(EntityCondition[] conditions) {
		this.conditions = conditions;
	}

	public EntityStatus[] getConsequences() {
		return consequences;
	}

	public void setConsequences(EntityStatus[] consequences) {
		this.consequences = consequences;
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
