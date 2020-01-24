package com.unimib.common;

import com.unimib.smarthome.entity.EntityManager;



public interface Observer {
	public void updateAdd(EntityManager entityManager);
	public void updateRemove(EntityManager entityManager);
	

}
