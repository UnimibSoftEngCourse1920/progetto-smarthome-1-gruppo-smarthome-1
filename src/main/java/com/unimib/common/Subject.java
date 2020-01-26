package com.unimib.common;

import com.unimib.smarthome.entity.Entity;

public interface Subject {

	public void attach(Observer o);
	public boolean detach(Observer o);
	
	public void notifyObservers(Entity entity);


}
