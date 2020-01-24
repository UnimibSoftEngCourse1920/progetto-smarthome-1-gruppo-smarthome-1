package com.unimib.smarthome.entity.enums;
public enum EntityType {
	BINARY("BINARY"), 
	STATE("STATE"), 
	RANGE("RANGE");

	private String name;
	
	EntityType(String string) {
		name = string;
	}
	
	public String getName(){
		return name;
	}
	
	public EntityType getEntityType(String s) {
		for(EntityType et : EntityType.values()) {
			if(et.getName().equalsIgnoreCase(s))
				return et;
		}
		
		return null;
	}
	
	
}
