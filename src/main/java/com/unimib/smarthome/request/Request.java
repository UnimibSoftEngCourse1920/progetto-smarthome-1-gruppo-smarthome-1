package com.unimib.smarthome.request;
import java.util.Arrays;


public class Request implements Comparable<Request>{

	private EntityCondition[] conditions;
	private EntityStatus[] consequences;
	private boolean retain;
	private int priority;
	private boolean askPermission;
	
	public Request(EntityCondition[] conditions, EntityStatus[] consequences) {
		this(conditions, consequences, false, 1, false);
	} 
	
	public Request(EntityCondition[] conditions, EntityStatus[] consequences, boolean retain, int priority){
		this(conditions, consequences, retain, priority, false);
	}
	
	public Request(EntityCondition[] conditions, EntityStatus[] consequences, boolean retain, int priority, boolean askPermission){
		this.conditions = conditions;
		this.consequences = consequences;
		this.retain = retain;
		this.priority = priority;
		this.askPermission = askPermission;
	}

	public EntityCondition[] getConditions() {
		return conditions;
	}

	public EntityStatus[] getConsequences() {
		return consequences;
	}

	public boolean getRetain() {
		return retain;
	}

	public int getPriority() {
		return priority;
	}
	
	public boolean getAskPermission() {
		return askPermission;
	}
	@Override
	public int compareTo(Request r2) {
		Integer p1 = Integer.valueOf(this.getPriority());
		Integer p2 = Integer.valueOf(r2.getPriority());
		return p1.compareTo(p2);
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "Request " + this.hashCode() + " [conditions: " + Arrays.toString(getConditions()) + ", consequences: " + Arrays.toString(getConsequences()) + ", retain: " + this.getRetain() + ", priority: " + this.getPriority() + "]";
	}

	
	
	
	
	
	
}
