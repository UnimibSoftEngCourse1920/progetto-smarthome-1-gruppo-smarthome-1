package com.unimib.smarthome.request;

public class Request implements Comparable<Request>{
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

	public void addAction(EntityStatus action) {}
	
	public void executeRequest() {}

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
	
	

	@Override
	public int compareTo(Request r2) {
		Integer p1 = Integer.valueOf(this.getPriority());
		Integer p2 = Integer.valueOf(r2.getPriority());
		return p1.compareTo(p2);
	}
	
	
	
	
	
	
	
}
