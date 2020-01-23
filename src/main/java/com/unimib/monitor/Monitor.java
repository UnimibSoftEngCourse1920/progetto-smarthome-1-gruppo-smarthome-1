package com.unimib.monitor;


import java.util.HashMap;



import com.unimib.common.Observer;
import com.unimib.emac.Emac;
import com.unimib.test.Entity;




public class Monitor implements Observer {
	static Monitor instance;
	private Emac emac;
	private HashMap<Integer, Entity> mappedEntity;
	
	
	
	
	public HashMap<Integer, Entity> getMappedEntity() {
		return mappedEntity;
	}
	

	public void setMappedEntity(HashMap<Integer, Entity> mappedEntity) {
		this.mappedEntity = mappedEntity;
	}


	private Monitor() {
		this.setMappedEntity(new HashMap<Integer, Entity>());
	}
	
	//Singleton
	public static Monitor getInstance() {
		if(instance == null) {
			instance = new Monitor();
		}
		return instance;
	}
	
	@Override
	public void updateAdd(Entity e) {
		mappedEntity.put(e.hashCode(), e);
	}
	
	@Override
	public void updateRemove(Entity e) {
		mappedEntity.remove(e.hashCode(), e);
	}
}	


