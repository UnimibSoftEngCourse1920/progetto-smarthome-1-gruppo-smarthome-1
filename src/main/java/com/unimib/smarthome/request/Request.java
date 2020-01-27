package com.unimib.smarthome.request;
import java.util.Arrays;


public class Request implements Comparable<Request>{

	private EntityCondition[] conditions;
	private EntityStatus[] consequences;
	private boolean retain;
	private int priority;
	
	

	public Request(EntityCondition[] conditions, EntityStatus[] consequences, boolean retain, int priority ){
		this.setConditions(conditions);
		this.setConsequences(consequences);
		this.setRetain(retain);
		this.setPriority(priority);
	}
	// da cancellare successivamente, una volta finite le modifiche di @gianlo e @davide.
	public void executeRequest() {
		
	}
	public EntityCondition[] getConditions() {
		return conditions;
	}

	public void setConditions(EntityCondition[] conditions) {
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
	public boolean equals(Object obj) {
		return super.equals(obj);
	}


	@Override
	public int compareTo(Request r2) {
		Integer p1 = Integer.valueOf(this.getPriority());
		Integer p2 = Integer.valueOf(r2.getPriority());
		return p1.compareTo(p2);
	}
	
	

	@Override
	public String toString() {
		return "Request " + this.hashCode() + " [conditions: " + Arrays.toString(getConditions()) + ", consequences: " + Arrays.toString(getConsequences()) + ", retain: " + this.getRetain() + ", priority: " + this.getPriority() + "]";
	}

	
	
	
	
	
	
}
